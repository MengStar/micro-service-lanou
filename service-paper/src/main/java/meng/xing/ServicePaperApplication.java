package meng.xing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import meng.xing.entity.Exam;
import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
import meng.xing.repository.ExamRepository;
import meng.xing.repository.PaperRepository;
import meng.xing.repository.SubjectRepository;
import meng.xing.repository.TestItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


@SpringBootApplication
@EnableDiscoveryClient
public class ServicePaperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePaperApplication.class, args);
    }
}

@RefreshScope
@Component
class DatabaseLoader implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Value("${subjectList}")
    private String[] subjectList;

    private final
    SubjectRepository subjectRepository;
    private final
    PaperRepository paperRepository;
    private final
    TestItemRepository testItemRepository;
    private final ExamRepository examRepository;

    @Autowired
    public DatabaseLoader(SubjectRepository subjectRepository, PaperRepository paperRepository, TestItemRepository testItemRepository, ExamRepository examRepository) {
        this.subjectRepository = subjectRepository;
        this.paperRepository = paperRepository;
        this.testItemRepository = testItemRepository;
        this.examRepository = examRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        logger.info("初始化类别表...");
        if (subjectRepository.count() != 0)
            return;
        for (String subject : subjectList) {
            System.out.println(subject);
            subjectRepository.save(new Subject(subject));
        }

        logger.info("初始化题库表...");
        Map<String, String> items = new HashMap<>();
        items.put("A", "asda");
        items.put("B", "ds");
        items.put("C", "sd");
        items.put("D", "asdda");
        ChoiceItem choiceItem = new ChoiceItem("题干", items);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(choiceItem);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        TestItem t1 = new TestItem(TestItemType.QUESTION.toString(), "请简述JAVAb编译过程", "答案无");
        TestItem t2 = new TestItem(TestItemType.CHOICE.toString(), json, "A");
        t1.setSubject(subjectRepository.findByType("JAVA"));
        t2.setSubject(subjectRepository.findByType("WEB"));
        testItemRepository.save(t1);
        testItemRepository.save(t2);

        logger.info("初始化试卷表...");
        Set<TestItem> testItems = new HashSet<>();
        testItems.addAll(testItemRepository.findAll());
        Subject subject = subjectRepository.findByType("JAVA");
        paperRepository.save(new Paper("admin", "测试试卷", subject, testItems));

        logger.info("初始化考试表...");
        Subject java = subjectRepository.findByType("JAVA");
        Subject scala = subjectRepository.findByType("Scala");
        Paper paper = paperRepository.findOne((long) 1);
        for (int i = 0; i < 20; i++) {
            examRepository.save(new Exam("这是一场测试考试" + i, java, paper, "admin"));
        }
        for (int i = 0; i < 20; i++) {
            examRepository.save(new Exam("这是一场测试考试" + i, scala, paper, "admin"));
        }

        logger.info("service-paper微服务 api文档: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort() + "/swagger-ui.html");
    }

}

@Configuration
@EnableSwagger2
class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("meng.xing.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("service-paper APIs")
                .description("service-paper微服务api文档")
                .contact(new Contact("刘星", "https://github.com/MengStar", "641510128@qqq.com"))
                .version("0.0.1-SNAPSHOT")
                .build();
    }


}

@Configuration
class ServiceInfoUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private static EmbeddedServletContainerInitializedEvent event;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        ServiceInfoUtil.event = event;
    }

    static int getPort() {
        try {
            return event.getEmbeddedServletContainer().getPort();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    static String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}

/**
 * 配置懒加载，否则会出现循环加载的错误
 * 同时需要在 bootstrap 配置文件中配置spring.jpa.hibernate.open-in-view: true
 */
@Configuration
class LazyLoadConfig extends WebMvcConfigurerAdapter {
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();

        // Registering Hibernate4Module to support lazy objects
        mapper.registerModule(new Hibernate4Module());

        messageConverter.setObjectMapper(mapper);

        return messageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add the custom-configured HttpMessageConverter
        converters.add(jacksonMessageConverter());

        super.configureMessageConverters(converters);
    }
}

