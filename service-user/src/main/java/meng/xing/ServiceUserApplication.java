package meng.xing;

import meng.xing.entity.User;
import meng.xing.entity.UserRole;
import meng.xing.repository.UserRoleRepository;
import meng.xing.service.UserRoleService;
import meng.xing.service.UserService;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableDiscoveryClient
public class ServiceUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}

@RefreshScope
@Component
class DatabaseLoader implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Value("${developUser.username}")
    private String username;
    @Value("${developUser.password}")
    private String password;
    @Value("${userRoleList}")
    private String[] userRoleList;

    @Value("${defaultUserRole}")
    private String[] defaultUserRole;

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleService userRoleService;

    @Autowired
    public DatabaseLoader(UserService userService, UserRoleRepository userRoleRepository, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("初始化权限表...新增权限如下：");
        Arrays.asList(userRoleList).forEach(System.out::println);
        //初始化权限表
        if (userRoleRepository.count() != 0)
            return;
        for (String anUserRoleList : userRoleList) {
            userRoleRepository.save(new UserRole(anUserRoleList));
        }
        logger.info("新增开发用户...");
        //新增开发用户
        User testUser = new User(username, password, "萌萌", "13086695953", "64151@qq.com", "四川省 成都市 郫县", true, 18);
        Set<UserRole> _roles = new HashSet<>();
        for (String role : userRoleList) {
            _roles.add(userRoleService.findUserRoleByRole(role));
        }
        testUser.setRoles(_roles);
        userService.register(testUser);

        logger.info("service-user微服务 api文档: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort() + "/swagger-ui.html");
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
                .title("service-user APIs")
                .description("service-user微服务api文档")
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

