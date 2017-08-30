package meng.xing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class UserController {
    @Value("${returnStr}")
    private String ret;

    @GetMapping("/")
    public String hello() {
        return "service-user " + this.ret;
    }
}
