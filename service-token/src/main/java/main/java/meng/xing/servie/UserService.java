package main.java.meng.xing.servie;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient("service-user")
public interface UserService {
    @RequestMapping(value = "/users/{username}/password", method = RequestMethod.GET)
    String getPasswordByUsername(@PathVariable("username") String username);

    @RequestMapping(value = "/users/{username}/password", method = RequestMethod.GET)
    Date getLastPasswordResetByUsername(@PathVariable("username") String username);
}
