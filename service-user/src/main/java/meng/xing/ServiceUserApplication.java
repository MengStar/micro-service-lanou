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

    @Value("${developUser}.${username}")
    private String username;
    @Value("${developUser}.${password}")
    private String password;
    @Value("${userRoleList}")
    private String[] userRoleList;

    @Autowired
    UserService userService;
    @Autowired
    UserRoleRepository userRoleRepository;

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
        userService.setUserRoles(username,userRoleList);
    }
}