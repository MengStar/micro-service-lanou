package meng.xing;

import meng.xing.entity.User;
import meng.xing.entity.UserRole;
import meng.xing.repository.UserRoleRepository;
import meng.xing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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

    @Value("${developUser.username}")
    private String username;
    @Value("${developUser.password}")
    private String password;
    @Value("${userRoleList}")
    private String[] userRoleList;

    final UserService userService;
    final UserRoleRepository userRoleRepository;

    @Autowired
    public DatabaseLoader(UserService userService, UserRoleRepository userRoleRepository) {
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        //初始化权限表
        if (userRoleRepository.count() != 0)
            return;
        for (String anUserRoleList : userRoleList) {
            userRoleRepository.save(new UserRole(anUserRoleList));
        }
        //新增开发用户
        User testUser = new User(username, password, "萌萌", "13086695953", "6415@qq.com", "四川省 成都市 郫县", true, 18);
        userService.register(testUser);
        userService.setUserRoles(username, userRoleList);
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
                .termsOfServiceUrl("https://github.com/MengStar/micro-sevice-lanou/")
                .contact("刘星")
                .version("0.0.1-SNAPSHOT")
                .build();
    }

}

