package io.quacker.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
    private final Key key;

    public JwtTokenUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Access JWT 토큰 생성 (사용자 ID, 이메일 포함)
     * @param userId, 고유식별자
     * @param email, 참고
     * @param role, 참고
     * @return String, 접근토큰
     */
    public String generateAccessToken(Long userId, String email, String role) {
        String jwtId = UUID.randomUUID().toString();
        return Jwts.builder()
                .setClaims(Map.of("email", email, "role", role)) // email 저장
                .setSubject(userId.toString()) // 사용자 id 저장,
                .setId(jwtId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Access JWT 토큰 생성 (사용자 ID, 이메일 포함)
     * @param userId, 고유식별자 sub
     * @return String, 접근토큰
     */
    public String generateRefreshToken(Long userId) {
        String jwtId = UUID.randomUUID().toString();
        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 id 저장,
                .setId(jwtId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
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
     * JWT 토큰에서 jwi 추출
     */
    public String extractId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    public Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role")
                .toString();
    }

    /**
     * 접근 토큰이 유효한지, 만료되지 않았는지 검증
     */
    public boolean validateAccessToken(String token, Long userId) {
        return extractUserId(token).equals(userId) && !isTokenExpired(token);
    }

    /**
     * 토큰 만료 확인
     */
    public boolean isTokenExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration() // exp
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

}
