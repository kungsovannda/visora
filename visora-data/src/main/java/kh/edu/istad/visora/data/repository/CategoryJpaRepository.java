package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {
    Optional<CategoryEntity> findByUuid(String uuid);

    Optional<CategoryEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}