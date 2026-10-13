package kh.edu.istad.visora.application.dto.favorite.response;

import kh.edu.istad.visora.domain.entity.enums.MainType;

import java.sql.Timestamp;

public record FavoriteResponse(

        String uuid,

        String ownerUuid,

        MainType type,

        String targetUuid,

        Timestamp createdAt

) {
}
