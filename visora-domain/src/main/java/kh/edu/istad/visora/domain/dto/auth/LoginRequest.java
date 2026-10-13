package kh.edu.istad.visora.domain.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}