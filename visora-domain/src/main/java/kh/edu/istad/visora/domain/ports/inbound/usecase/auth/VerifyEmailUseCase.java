package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.VerifyEmailRequest;

public interface VerifyEmailUseCase {

    void verifyEmail(VerifyEmailRequest request);

}