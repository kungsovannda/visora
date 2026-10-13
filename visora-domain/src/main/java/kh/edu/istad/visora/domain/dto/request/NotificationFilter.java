package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.entity.enums.NotificationType;
import kh.edu.istad.visora.domain.enums.NotificationSort;

public record NotificationFilter(
        int pageNumber,
        int pageSize,
        NotificationType type,
        Boolean read,
        NotificationSort sort
) {
}
