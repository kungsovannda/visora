package kh.edu.istad.visora.domain.dto.auth;

public record AuthTokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}