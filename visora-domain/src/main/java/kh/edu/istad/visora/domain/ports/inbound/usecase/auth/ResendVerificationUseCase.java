package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.ResendVerificationRequest;

public interface ResendVerificationUseCase {

    void resendVerification(ResendVerificationRequest request);

}