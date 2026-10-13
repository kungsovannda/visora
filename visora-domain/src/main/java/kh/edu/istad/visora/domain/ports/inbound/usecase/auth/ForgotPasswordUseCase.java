package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.ForgotPasswordRequest;

public interface ForgotPasswordUseCase {

    void forgotPassword(ForgotPasswordRequest request);

}