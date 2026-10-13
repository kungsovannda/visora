package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.StorageEntity;
import kh.edu.istad.visora.data.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageJpaRepository extends JpaRepository<StorageEntity, Long> {

    Optional<StorageEntity> findByFileName(String fileName);

    boolean existsByFileName(String fileName);

    Page<StorageEntity> findAllByOwner(Pageable pageable, UserEntity owner);
}