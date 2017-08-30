package meng.xing;

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
    private String username;
    @Value("${password}")
    private String password;
    @Value("${userRoleList}")
    private String[] userRoleList;

    @Autowired
    UserService userService;
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public void run(String... strings) throws Exception {
        if (userRoleRepository.count() != 0)
            return;
        for (String anUserRoleList : userRoleList) {
            userRoleRepository.save(new UserRole(anUserRoleList));
        }
    }
}