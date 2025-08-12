package big.data.bigdata.utils;

import big.data.bigdata.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    //生成JWT
    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",user.getRole());//用户的角色字段
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expiration*1000))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }
    //从JWT当中提取用户名
    public String extractUsername(String token){
        return extractClaim (token, Claims::getSubject);
    }

    //验证JWT令牌
    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            //可以记录日志处理异常
            return false;
        }
    }
    private <T> T extractClaim(String token, Function <Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
