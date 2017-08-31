package meng.xing.servie;

public interface TokenService {
    String getToken(String username, String password);

    String getUsernameFromToken(String token);
}
