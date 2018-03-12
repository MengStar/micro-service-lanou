package meng.xing.tokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import meng.xing.servie.remote.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Web Token（JWT）的处理类
 * token 信息：
 * 1.用户名
 * 2.创建时间
 * 3.过期时间
 * token 非法：
 * 1.无法解析
 * 2.过期时间 小于 当前系统时间
 * 3.创建时间 小于 密码修改时间
 */
@Component
public class JwtTokenUtil {
    private Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "a"; //用户名key
    private static final String CLAIM_KEY_CREATED = "b";//创建时间key
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;
    private final UserService userService;

    @Autowired
    public JwtTokenUtil(UserService userService) {
        this.userService = userService;
    }

    public Date getCreatedDateFromToken(String token) {
        token = subTokenHead(token);
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        token = subTokenHead(token);
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getUsernameFromToken(String token) {
        token = subTokenHead(token);
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = String.valueOf(claims.get(CLAIM_KEY_USERNAME));
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put(CLAIM_KEY_USERNAME, getUsernameFromToken(token));
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {

        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);

        final Date lastPasswordResetDate = userService.getLastPasswordResetByUsername(username);
        logger.info("获取用户" + username + "最后修改密码的时间" + lastPasswordResetDate);
        return
                username != null
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, lastPasswordResetDate);
    }

    private String generateToken(Map<String, Object> claims) {
        return tokenPrefix + Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private String subTokenHead(String token) {
        return token.substring(tokenPrefix.length());
    }
}
