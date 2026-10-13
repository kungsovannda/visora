package kh.edu.istad.visora.domain.dto.auth;

public record ResetPasswordRequest(
        String token,
        String newPassword,
        String confirmPassword
) {
}