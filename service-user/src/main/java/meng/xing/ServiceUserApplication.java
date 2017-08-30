package meng.xing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient

public class ServiceUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUserApplication.class, args);
	}
}

@RefreshScope
class DatabaseLoader implements CommandLineRunner {
	@Override
	public void run(String... strings) throws Exception {

	}
}