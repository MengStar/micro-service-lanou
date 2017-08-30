package meng.xing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

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

	@Value("${username}")
	private final String username = "admin";
	@Value("${password}")
	private final String password = "admin";
	@Override
	public void run(String... strings) throws Exception {
	}
}