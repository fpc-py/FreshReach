package com.takeout.xianda.utils;

import com.takeout.xianda.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
@Component
public class JwtUtil {
    private static final String secretKey = JwtConstant.SECRET_KEY;
    private static final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public static String createJwt(String secretKey, long ttlMillis, Map<String,Object> claims){
        //创建密钥

        //创建时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + ttlMillis);
        //创建JWT
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();

    }

    public static Claims parseJwt(String secretKey, String token){
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 获取JWT的过期时间
    public static long getExpireTime(String token){
        return parseJwt(JwtConstant.SECRET_KEY, token).getExpiration().getTime();
    }

    //验证token
    public boolean verifyToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //从token中获取用户id
    public Long getUserId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object obj = claims.get("userId");
        if (obj == null){
            throw new RuntimeException("token缺失userid,请重新登录");
        }
        return Long.valueOf(obj.toString());

    }
}
