package kh.edu.istad.visora.domain.ports.inbound.usecase.user;

import kh.edu.istad.visora.domain.entity.User;

public interface CreateUserUseCase {

    User createUser(User user);

}
