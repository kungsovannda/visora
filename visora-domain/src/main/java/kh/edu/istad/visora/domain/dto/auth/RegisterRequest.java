package kh.edu.istad.visora.domain.dto.auth;

import kh.edu.istad.visora.domain.exception.auth.PasswordNotMatchException;

public record RegisterRequest(
        String givenName,
        String familyName,
        String username,
        String email,
        String phoneNumber,
        String password,
        String confirmPassword
) {

    public void validatePassword() {
        if (!this.password.equals(this.confirmPassword)) {
            throw new PasswordNotMatchException("Password and confirm password do not match");
        }
    }
}