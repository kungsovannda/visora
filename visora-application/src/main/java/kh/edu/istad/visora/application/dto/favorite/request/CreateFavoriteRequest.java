package kh.edu.istad.visora.application.dto.favorite.request;

public record CreateFavoriteRequest(

        String templateUuid,

        String backdropUuid

) {
}
