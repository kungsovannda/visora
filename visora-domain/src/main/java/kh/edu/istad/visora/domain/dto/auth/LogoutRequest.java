package kh.edu.istad.visora.domain.dto.auth;

public record LogoutRequest(
        String refreshToken
) {
}