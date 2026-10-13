package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.AuthTokenResponse;
import kh.edu.istad.visora.domain.dto.auth.LoginRequest;

public interface LoginUseCase {

    AuthTokenResponse login(LoginRequest request);

}