package io.quacker.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private final Long expiration;
    private final Key key;

    public JwtTokenUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
    }

    /**
     * JWT 토큰 생성 (사용자 ID, 이메일 포함)
     */
    public String generateToken(Long userId, String email, String name) {
        return Jwts.builder()
                .setClaims(Map.of("email", email, "name", name)) // email 저장
                .setSubject(userId.toString()) // 사용자 id 저장,
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * sub으로 사용자 id 사용
     * JWT 토큰에서 사용자 ID 추출
     */
    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()); // sub
    }
    /**
     * 비공개 claim email 사용
     * JWT 토큰에서 사용자 email 추출
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email")
                .toString();
    }

    /**
     * 토큰이 유효한지, 만료되지 않았는지 검증
     */
    public boolean validateToken(String token, Long userId) {
        return extractUserId(token).equals(userId) && !isTokenExpired(token);
    }

    /**
     * 토큰 만료 확인
     */
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration() // exp
                .before(new Date());
    }
}
