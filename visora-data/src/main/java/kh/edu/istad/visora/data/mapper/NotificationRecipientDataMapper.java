package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.NotificationRecipientEntity;
import kh.edu.istad.visora.domain.entity.NotificationRecipient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDataMapper.class, NotificationDataMapper.class})
public interface NotificationRecipientDataMapper {

    NotificationRecipient toDomain(NotificationRecipientEntity entity);

    NotificationRecipientEntity toEntity(NotificationRecipient domain);

}
