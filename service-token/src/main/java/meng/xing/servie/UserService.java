package meng.xing.servie;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Primary
@FeignClient(name = "service-user", fallback = UserServiceFallback.class)
public interface UserService {

    @RequestMapping(value = "/users/{username}/lastPasswordResetDate", method = RequestMethod.GET)
    Date getLastPasswordResetByUsername(@PathVariable("username") String username);

    @RequestMapping(value = "/users/{username}/password", method = RequestMethod.GET)
    String getCorrectPasswordByUsername(@PathVariable("username") String username);

}
