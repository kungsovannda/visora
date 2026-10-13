package kh.edu.istad.visora.domain.ports.inbound.usecase.user.adapter;

import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteUserUseCaseAdapter implements DeleteUserUseCase {

    private final UserRepository userRepository;

    @Override
    public void deleteUserByUuid(String uuid) {
        userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteByUuid(uuid);
    }

}
