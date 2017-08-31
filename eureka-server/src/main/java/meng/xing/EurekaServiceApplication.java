package meng.xing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {
    private final static Logger logger = LoggerFactory.getLogger(EurekaServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
        logger.info("eureka-server服务 管理页面: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort());
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