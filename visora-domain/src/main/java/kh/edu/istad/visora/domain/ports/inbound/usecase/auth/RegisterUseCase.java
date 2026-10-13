package kh.edu.istad.visora.domain.ports.inbound.usecase.auth;

import kh.edu.istad.visora.domain.dto.auth.RegisterRequest;
import kh.edu.istad.visora.domain.dto.auth.RegisterResponse;

public interface RegisterUseCase {

    RegisterResponse register(RegisterRequest request);

}
