package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByUuid(String uuid);

    List<RoleEntity> findAllByIsDeletedFalse();

    Page<RoleEntity> findAllByIsDeletedFalse(Pageable pageable);

    boolean existsByRole(String role);

    void deleteByUuid(String uuid);

}
