package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.BackdropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BackdropJpaRepository extends JpaRepository<BackdropEntity, Long>, JpaSpecificationExecutor<BackdropEntity> {

    Optional<BackdropEntity> findByUuid(String uuid);

}