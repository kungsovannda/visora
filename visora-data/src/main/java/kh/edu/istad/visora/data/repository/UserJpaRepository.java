package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUuid(String uuid);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByIsDeletedFalse();

    List<UserEntity> findAllByRoleRoleIgnoreCaseAndIsDeletedFalse(String role);

    Page<UserEntity> findAllByIsDeletedFalse(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByUuid(String uuid);

}