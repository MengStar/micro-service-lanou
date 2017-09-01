package meng.xing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
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
import org.springframework.stereotype.Component;
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
    private List<String> subjectList;

    private final
    SubjectRepository subjectRepository;
    private final
    PaperRepository paperRepository;
    private final
    TestItemRepository testItemRepository;

    @Autowired
    public DatabaseLoader(SubjectRepository subjectRepository, PaperRepository paperRepository, TestItemRepository testItemRepository) {
        this.subjectRepository = subjectRepository;
        this.paperRepository = paperRepository;
        this.testItemRepository = testItemRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        logger.info("初始化类别表...");
        initSubject();//初始化类别表

        logger.info("初始化题库表...");
        initTestItem(); //初始化题库表

        logger.info("初始化试卷表...");
        initPaper(); //初始化试卷表

        logger.info("service-paper微服务 api文档: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort() + "/swagger-ui.html");
    }

    private void initSubject() {
        if (subjectRepository.count() != 0)
            return;
        subjectList.forEach(subject -> subjectRepository.save(new Subject(subject)));

    }


    private void initTestItem() {
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
    }

    private void initPaper() {
        Set<TestItem> items = new HashSet<>();
        items.addAll(testItemRepository.findAll());
        Subject subject = subjectRepository.findByType("JAVA");
        paperRepository.save(new Paper("admin", "测试试卷", subject, items));
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
        return event.getEmbeddedServletContainer().getPort();
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

class ChoiceItem {
    private String question;
    private Map<String, String> answer;

    public ChoiceItem(String question, Map<String, String> answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getAnswer() {
        return answer;
    }

    public void setAnswer(Map<String, String> answer) {
        this.answer = answer;
    }
}
