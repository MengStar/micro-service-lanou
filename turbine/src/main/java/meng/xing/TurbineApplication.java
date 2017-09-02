package meng.xing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableTurbine
@EnableDiscoveryClient
public class TurbineApplication {

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(TurbineApplication.class);
        SpringApplication.run(TurbineApplication.class, args);
        logger.info("turbine 微服务: " + "http://" + ServiceInfoUtil.getHost() + ":" + ServiceInfoUtil.getPort() + "/turbine.stream");
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
