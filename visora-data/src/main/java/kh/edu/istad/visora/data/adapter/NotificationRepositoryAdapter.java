package kh.edu.istad.visora.data.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.visora.data.entity.NotificationEntity;
import kh.edu.istad.visora.data.entity.NotificationRecipientEntity;
import kh.edu.istad.visora.data.entity.UserEntity;
import kh.edu.istad.visora.data.mapper.NotificationDataMapper;
import kh.edu.istad.visora.data.mapper.NotificationRecipientDataMapper;
import kh.edu.istad.visora.data.repository.NotificationJpaRepository;
import kh.edu.istad.visora.data.repository.NotificationRecipientJpaRepository;
import kh.edu.istad.visora.data.repository.UserJpaRepository;
import kh.edu.istad.visora.domain.dto.request.NotificationFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Notification;
import kh.edu.istad.visora.domain.entity.NotificationRecipient;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.NotificationSort;
import kh.edu.istad.visora.domain.ports.outbound.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationRecipientJpaRepository notificationRecipientJpaRepository;
    private final NotificationDataMapper notificationDataMapper;
    private final NotificationRecipientDataMapper notificationRecipientDataMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public Notification save(Notification notification) {
        NotificationEntity entity = notificationDataMapper.toEntity(notification);
        entity.setRecipients(new ArrayList<>());
        NotificationEntity saved = notificationJpaRepository.save(entity);

        List<NotificationRecipientEntity> rows = new ArrayList<>();
        for (NotificationRecipient recipient : notification.getRecipients()) {
            NotificationRecipientEntity row = notificationRecipientDataMapper.toEntity(recipient);
            row.setNotification(saved);
            row.setRecipient(requireUserEntity(recipient.getRecipient()));
            rows.add(row);
        }
        notificationRecipientJpaRepository.saveAll(rows);
        saved.setRecipients(rows);

        Notification result = notificationDataMapper.toDomain(saved);
        result.setRecipients(notification.getRecipients());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findByUuid(String uuid) {
        return notificationJpaRepository.findByUuidAndIsDeletedFalse(uuid)
                .map(notificationDataMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationRecipient> findForRecipient(User recipient, NotificationFilter filter) {
        var pageResponse = notificationRecipientJpaRepository.findAll(
                buildSpec(recipient, filter),
                PageRequest.of(filter.pageNumber(), filter.pageSize(), toSort(filter.sort()))
        );

        List<NotificationRecipient> contents = pageResponse.getContent().stream()
                .map(notificationRecipientDataMapper::toDomain)
                .toList();

        return new Page<>(
                contents,
                pageResponse.getNumber(),
                pageResponse.getSize(),
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationRecipient> findRecipientRow(String notificationUuid, User recipient) {
        return notificationRecipientJpaRepository
                .findByNotificationUuidAndRecipientUuidAndIsDeletedFalse(notificationUuid, recipient.getUuid())
                .map(notificationRecipientDataMapper::toDomain);
    }

    @Override
    public long countUnreadForRecipient(User recipient) {
        return notificationRecipientJpaRepository
                .countByRecipientUuidAndReadFalseAndIsDeletedFalse(recipient.getUuid());
    }

    @Override
    @Transactional
    public NotificationRecipient saveRecipient(NotificationRecipient notificationRecipient) {
        NotificationRecipientEntity row = notificationRecipientDataMapper.toEntity(notificationRecipient);
        row.setRecipient(requireUserEntity(notificationRecipient.getRecipient()));
        if (notificationRecipient.getNotification() != null) {
            row.setNotification(requireNotificationEntity(notificationRecipient.getNotification()));
        }
        return notificationRecipientDataMapper.toDomain(notificationRecipientJpaRepository.save(row));
    }

    @Override
    @Transactional
    public int markAllReadForRecipient(User recipient) {
        return notificationRecipientJpaRepository.markAllReadForRecipient(recipient.getUuid());
    }

    private Specification<NotificationRecipientEntity> buildSpec(User recipient, NotificationFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            predicates.add(criteriaBuilder.equal(root.get("recipient").get("uuid"), recipient.getUuid()));

            if (filter.read() != null) {
                predicates.add(criteriaBuilder.equal(root.get("read"), filter.read()));
            }

            if (filter.type() != null) {
                predicates.add(criteriaBuilder.equal(root.get("notification").get("type"), filter.type()));
            }

            boolean isCountQuery = query != null && Long.class.equals(query.getResultType());
            if (!isCountQuery && query != null) {
                root.fetch("notification");
                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort toSort(NotificationSort sort) {
        return switch (sort == null ? NotificationSort.NEWEST : sort) {
            case NEWEST -> JpaSort.unsafe(Sort.Direction.DESC, "notification.createdAt", "id");
            case OLDEST -> JpaSort.unsafe(Sort.Direction.ASC, "notification.createdAt", "id");
        };
    }

    private UserEntity requireUserEntity(User user) {
        if (user == null) {
            throw new IllegalStateException("Notification recipient user is required");
        }
        if (user.getId() <= 0) {
            throw new IllegalStateException(
                    "Notification recipient user must be persisted: " + user.getUuid());
        }
        return userJpaRepository.getReferenceById(user.getId());
    }

    private NotificationEntity requireNotificationEntity(Notification notification) {
        if (notification == null) {
            throw new IllegalStateException("Notification is required");
        }
        if (notification.getId() > 0) {
            return notificationJpaRepository.getReferenceById(notification.getId());
        }
        return notificationJpaRepository.findByUuidAndIsDeletedFalse(notification.getUuid())
                .orElseThrow(() -> new IllegalStateException(
                        "Notification was not found: " + notification.getUuid()));
    }

}
