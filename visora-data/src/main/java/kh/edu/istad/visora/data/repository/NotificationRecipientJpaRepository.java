package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.NotificationRecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRecipientJpaRepository
        extends JpaRepository<NotificationRecipientEntity, Long>,
        JpaSpecificationExecutor<NotificationRecipientEntity> {

    Optional<NotificationRecipientEntity> findByNotificationUuidAndRecipientUuidAndIsDeletedFalse(
            String notificationUuid,
            String recipientUuid);

    long countByRecipientUuidAndReadFalseAndIsDeletedFalse(String recipientUuid);

    @Modifying
    @Query(value = """
            update NotificationRecipientEntity r
               set r.read = true,
                   r.readAt = current_timestamp
             where r.recipient.uuid = :recipientUuid
               and r.read = false
               and r.isDeleted = false
            """)
    int markAllReadForRecipient(@Param("recipientUuid") String recipientUuid);

}
