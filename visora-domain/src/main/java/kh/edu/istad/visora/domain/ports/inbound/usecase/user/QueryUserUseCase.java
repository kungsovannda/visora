package kh.edu.istad.visora.domain.ports.inbound.usecase.user;

import kh.edu.istad.visora.domain.dto.request.UserFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.User;

import java.util.List;

public interface QueryUserUseCase {

    Page<User> getUsers(int pageNumber, int pageSize);

    Page<User> getUsers(UserFilter filter);

    List<User> getUsers();

    User getUserByUuid(String uuid);

    User getUserByEmail(String email);

    User getCurrentUser();

}
