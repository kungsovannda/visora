package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.UserFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> saveAll(List<User> users);

    Page<User> getAllUsers(int pageNumber, int pageSize);

    Page<User> getAllUsers(UserFilter filter);

    List<User> getAllUsers();

    Optional<User> findUserByUuid(String uuid);

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByRole(String role);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByUuid(String uuid);

}
