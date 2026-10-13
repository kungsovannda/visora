package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    Optional<NotificationEntity> findByUuidAndIsDeletedFalse(String uuid);

}
