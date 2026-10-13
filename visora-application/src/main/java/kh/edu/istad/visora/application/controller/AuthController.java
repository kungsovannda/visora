package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.auth.RegisterRequest;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.mapper.AuthApplicationMapper;
import kh.edu.istad.visora.domain.dto.auth.AuthTokenResponse;
import kh.edu.istad.visora.domain.dto.auth.ForgotPasswordRequest;
import kh.edu.istad.visora.domain.dto.auth.LoginRequest;
import kh.edu.istad.visora.domain.dto.auth.LogoutRequest;
import kh.edu.istad.visora.domain.dto.auth.RefreshTokenRequest;
import kh.edu.istad.visora.domain.dto.auth.RegisterResponse;
import kh.edu.istad.visora.domain.dto.auth.ResendVerificationRequest;
import kh.edu.istad.visora.domain.dto.auth.ResetPasswordRequest;
import kh.edu.istad.visora.domain.dto.auth.VerifyEmailRequest;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@RestController
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final LogoutAllUseCase logoutAllUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ResendVerificationUseCase resendVerificationUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final AuthApplicationMapper authApplicationMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        var register = registerUseCase.register(authApplicationMapper.toRegisterDomain(request));
        var response = new ApiResponse<>(
                register,
                201,
                true,
                "Registration successful"
        );
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        var login = loginUseCase.login(request);
        var response = new ApiResponse<>(
                login,
                200,
                true,
                "Login successful"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        var refresh = refreshTokenUseCase.refresh(request);
        var response = new ApiResponse<>(
                refresh,
                200,
                true,
                "Token refreshed successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@Valid @RequestBody LogoutRequest request) {
        logoutUseCase.logout(request);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Logout successful"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<String>> logoutAll(@AuthenticationPrincipal User principal) {
        logoutAllUseCase.logoutAll(principal.getUuid());
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Logged out from all devices successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        verifyEmailUseCase.verifyEmail(request);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Email verified successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        resendVerificationUseCase.resendVerification(request);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Verification email sent successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.forgotPassword(request);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Password reset email sent successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.resetPassword(request);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Password reset successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

}