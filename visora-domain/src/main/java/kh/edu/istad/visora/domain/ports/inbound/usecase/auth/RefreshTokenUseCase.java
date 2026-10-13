package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.AuthTokenResponse;
import kh.edu.istad.visora.domain.dto.auth.RefreshTokenRequest;

public interface RefreshTokenUseCase {

    AuthTokenResponse refresh(RefreshTokenRequest request);

}