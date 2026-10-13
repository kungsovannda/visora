package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long>, JpaSpecificationExecutor<FavoriteEntity> {

    Optional<FavoriteEntity> findByUuidAndOwnerUuid(String uuid, String ownerUuid);

}
