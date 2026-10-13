package kh.edu.istad.visora.domain.ports;

import java.util.List;

public interface TokenProvider {

    String generateAccessToken(String userUuid, List<String> scopes, long expiresInSeconds);

    String generateRefreshToken(String userUuid, long expiresInSeconds);

    String extractUserUuid(String token);

    String extractTokenId(String token);

    boolean isTokenValid(String token);

    long getAccessTokenExpiresIn();

    long getRefreshTokenExpiresIn();

}