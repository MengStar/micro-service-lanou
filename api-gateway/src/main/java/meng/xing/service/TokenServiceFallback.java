package meng.xing.service;

import org.springframework.stereotype.Component;

@Component
public class TokenServiceFallback implements TokenService {
    @Override
    public String getToken(String username, String password) {
        return null;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return null;
    }
}
