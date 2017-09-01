package meng.xing.controller;

import meng.xing.servie.TokenService;
import meng.xing.servie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TokenController {
    private final
    TokenService tokenService;
    private final
    UserService userService;

    @Autowired
    public TokenController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping("/getUsername")
    public String getUsernameFromToken(@RequestParam("token") String token) {
        return tokenService.getUsernameFromToken(token);
    }


    @GetMapping("/getToken")
    public String getTokenFromUsername(@RequestParam("username") String username, @RequestParam("password") String password) {
        return tokenService.getToken(username, password);
    }


}
