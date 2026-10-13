package kh.edu.istad.visora.application.controller;

import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.notification.request.CreateNotificationRequest;
import kh.edu.istad.visora.application.dto.notification.response.CreatedNotificationResponse;
import kh.edu.istad.visora.application.dto.notification.response.NotificationResponse;
import kh.edu.istad.visora.application.mapper.NotificationApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.NotificationFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.enums.NotificationType;
import kh.edu.istad.visora.domain.enums.NotificationSort;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.CreateNotificationUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.MarkNotificationReadUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.QueryNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final QueryNotificationUseCase queryNotificationUseCase;
    private final MarkNotificationReadUseCase markNotificationReadUseCase;
    private final NotificationApplicationMapper notificationApplicationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getMyNotifications(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String sort
    ) {
        var filter = new NotificationFilter(
                Math.max(pageNumber, 0),
                pageSize <= 0 ? 25 : pageSize,
                type,
                read,
                resolveSort(sort)
        );
        var query = queryNotificationUseCase.getMyNotifications(filter);
        var page = new Page<>(
                query.contents().stream().map(notificationApplicationMapper::toResponse).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                page,
                HttpStatus.OK.value(),
                true,
                "Retrieved notifications data"
        ));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        var count = queryNotificationUseCase.getUnreadCount();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                count,
                HttpStatus.OK.value(),
                true,
                "Retrieved unread notification count"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatedNotificationResponse>> createNotification(
            @RequestBody CreateNotificationRequest request
    ) {
        var notification = createNotificationUseCase.create(
                request.type(),
                request.content(),
                request.recipientUuids(),
                request.recipientRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                notificationApplicationMapper.toCreatedResponse(notification),
                HttpStatus.CREATED.value(),
                true,
                "Notification created successfully"
        ));
    }

    @PatchMapping("/{notificationUuid}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable String notificationUuid
    ) {
        var updated = markNotificationReadUseCase.markAsRead(notificationUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                notificationApplicationMapper.toResponse(updated),
                HttpStatus.OK.value(),
                true,
                "Notification marked as read"
        ));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Integer>> markAllAsRead() {
        var updated = markNotificationReadUseCase.markAllAsRead();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                updated,
                HttpStatus.OK.value(),
                true,
                "Notifications marked as read"
        ));
    }

    private NotificationSort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return NotificationSort.NEWEST;
        }
        try {
            return NotificationSort.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NotificationSort.NEWEST;
        }
    }

}
