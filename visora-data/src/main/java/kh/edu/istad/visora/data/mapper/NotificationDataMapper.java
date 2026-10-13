package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.NotificationEntity;
import kh.edu.istad.visora.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationDataMapper {

    @Mapping(target = "recipients", ignore = true)
    Notification toDomain(NotificationEntity entity);

    @Mapping(target = "recipients", ignore = true)
    NotificationEntity toEntity(Notification domain);

}
