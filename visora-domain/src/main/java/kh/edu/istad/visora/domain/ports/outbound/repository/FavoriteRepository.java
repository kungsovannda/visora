package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.FavoriteFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Favorite;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.entity.enums.MainType;

import java.util.Optional;

public interface FavoriteRepository {

    Favorite save(Favorite favorite);

    Optional<Favorite> findByUuidAndOwner(String uuid, User owner);

    Optional<Favorite> findByOwnerAndTarget(User owner, MainType type, String targetUuid);

    Page<Favorite> findByOwner(User owner, FavoriteFilter filter);

    void delete(Favorite favorite);

}
