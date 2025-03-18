package io.quacker.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
//@RequiredArgsConstructor
public class JwtTokenUtil {

    // 우선 하드 코딩
//    @Value("$jwt.key")
    private static final String SECRET_KEY = "alksjdoiasnkljdbalskjdbalskjdhlakjshdljnalfgjksdbflkjshdfglksjndflkjsbgnldjkfbglskjdfbg";
    private static final long EXPIRATION = 15 * 60; // 15분

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * JWT 토큰 생성 (사용자 ID, 이메일 포함)
     */
    public String generateToken(Long userId, String email, String name) {
        return Jwts.builder()
                .setClaims(Map.of("email", email, "name", name)) // email 저장
                .setSubject(userId.toString()) // 사용자 id 저장,
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
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
                        .parseClaimsJwt(token)
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
                .parseClaimsJwt(token)
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
                .parseClaimsJwt(token)
                .getBody()
                .getExpiration() // exp
                .before(new Date());
    }
}
