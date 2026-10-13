package kh.edu.istad.visora.domain.ports.inbound.usecase.user;

import kh.edu.istad.visora.domain.entity.User;

public interface UpdateUserUseCase {

    User updateUser(String uuid, User user);

}
