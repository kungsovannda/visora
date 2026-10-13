package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.TemplateEntity;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateJpaRepository extends JpaRepository<TemplateEntity, Long>, JpaSpecificationExecutor<TemplateEntity> {

    Optional<TemplateEntity> findByUuid(String uuid);

    Optional<TemplateEntity> findBySourceBackdropUuidAndTemplateStatus(String backdropUuid, TemplateStatus templateStatus);

    @Query("""
            select count(t.id)
            from TemplateEntity t
            where t.isDeleted = false and t.category.uuid = :categoryUuid
            """)
    long countTemplatesByCategoryUuid(@Param("categoryUuid") String categoryUuid);

    @Query("""
            select t.category.uuid, count(t.id)
            from TemplateEntity t
            where t.isDeleted = false and t.category is not null
            group by t.category.uuid
            """)
    List<Object[]> countTemplatesGroupedByCategoryUuid();

}