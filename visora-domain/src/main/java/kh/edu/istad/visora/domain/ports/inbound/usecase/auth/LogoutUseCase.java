package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.LogoutRequest;

public interface LogoutUseCase {

    void logout(LogoutRequest request);

}