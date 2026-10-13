package kh.edu.istad.visora.domain.ports.inbound.usecase.user.adapter;

import kh.edu.istad.visora.domain.dto.request.UserFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QueryUserUseCaseAdapter implements QueryUserUseCase {

    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Override
    public Page<User> getUsers(int pageNumber, int pageSize) {
        return userRepository.getAllUsers(pageNumber, pageSize);
    }

    @Override
    public Page<User> getUsers(UserFilter filter) {
        return userRepository.getAllUsers(filter);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserByUuid(String uuid) {
        return userRepository.findUserByUuid(uuid).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getCurrentUser() {
        return currentUser.get();
    }
}
