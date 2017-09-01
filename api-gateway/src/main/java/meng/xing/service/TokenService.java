package meng.xing.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

@Primary
@FeignClient(name = "service-token", fallback = TokenServiceFallback.class)
public interface TokenService {
    @RequestMapping(value = "/token/getToken", method = RequestMethod.GET)
    String getToken(@RequestParam("username") String username, @RequestParam("password") String password);

    @RequestMapping(value = "/token/getUsername", method = RequestMethod.GET)
    String getUsernameFromToken(@RequestParam("token") String token);
}

