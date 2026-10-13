package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.ResetPasswordRequest;

public interface ResetPasswordUseCase {

    void resetPassword(ResetPasswordRequest request);

}