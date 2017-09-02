package meng.xing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@SpringBootApplication
@EnableDiscoveryClient
public class JoinExamApplication {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(JoinExamApplication.class);
        SpringApplication.run(JoinExamApplication.class, args);
        logger.info("service-joinExam微服务 api文档: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort() + "/swagger-ui.html");
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
                    .title("service-joinExam APIs")
                    .description("service-joinExam微服务api文档")
                    .contact(new Contact("刘星", "https://github.com/MengStar", "641510128@qqq.com"))
                    .version("0.0.1-SNAPSHOT")
                    .build();
        }

    }

    @Configuration
    static class ServiceInfoUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
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
}
