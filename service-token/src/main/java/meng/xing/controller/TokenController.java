package meng.xing.controller;

import meng.xing.servie.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final
    TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
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
