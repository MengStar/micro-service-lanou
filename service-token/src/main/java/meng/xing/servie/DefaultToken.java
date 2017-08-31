package meng.xing.servie;

import meng.xing.tokenUtil.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DefaultToken implements TokenService {
    private final
    UserService userService;
    private final
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    public DefaultToken(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String getToken(String username, String password) {
        if (Objects.equals(userService.getCorrectPasswordByUsername(username), password))
            return jwtTokenUtil.generateToken(username);
        return null;
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (!jwtTokenUtil.validateToken(token))
            return null;
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
