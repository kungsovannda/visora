package kh.edu.istad.visora.visora.infrastructure.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kh.edu.istad.visora.domain.ports.TokenProvider;
import kh.edu.istad.visora.visora.infrastructure.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider implements TokenProvider {

    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(String userUuid, List<String> scopes, long expiresInSeconds) {
        return Jwts.builder()
                .subject(userUuid)
                .claim("uid", userUuid)
                .claim("scope", scopes)
                .claim("type", "access")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiresInSeconds * 1000))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(String userUuid, long expiresInSeconds) {
        return Jwts.builder()
                .subject(userUuid)
                .claim("uid", userUuid)
                .claim("type", "refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiresInSeconds * 1000))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String extractUserUuid(String token) {
        return getPayload(token).get("uid", String.class);
    }

    @Override
    public String extractTokenId(String token) {
        return getPayload(token).getId();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            getPayload(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public long getAccessTokenExpiresIn() {
        return jwtProperties.getAccessTokenExpiration();
    }

    @Override
    public long getRefreshTokenExpiresIn() {
        return jwtProperties.getRefreshTokenExpiration();
    }

    private Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid or malformed JWT token", e);
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

}