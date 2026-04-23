//package com.hrbu.aidemo.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Component
//public class JWTUtil {
//
//    // 随便写一串长字符串
//    private static final String SECRET = "my-super-secure-secret-key-1234567890abcdef";
//    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1天
//
//    // 生成安全密钥（新版 JJWT 固定写法）
//    private SecretKey getKey() {
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//
//    // 生成Token
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(getKey())
//                .compact();
//    }
//
//    // 验证Token
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // 从token获取用户名
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//}