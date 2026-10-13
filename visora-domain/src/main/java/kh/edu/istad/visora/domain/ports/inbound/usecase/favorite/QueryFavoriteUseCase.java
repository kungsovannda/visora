package kh.edu.istad.visora.domain.ports.inbound.usecase.favorite;

import kh.edu.istad.visora.domain.dto.request.FavoriteFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Favorite;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteNotFoundException;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteValidationException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryFavoriteUseCase {

    private final FavoriteRepository favoriteRepository;
    private final CurrentUser currentUser;

    public Page<Favorite> getCurrentUserFavorites(FavoriteFilter filter) {
        return favoriteRepository.findByOwner(requireCurrentUser(), filter);
    }

    public Favorite getByUuid(String uuid) {
        return favoriteRepository.findByUuidAndOwner(uuid, requireCurrentUser())
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite was not found"));
    }

    private User requireCurrentUser() {
        User owner = currentUser.get();
        if (owner == null) {
            throw new FavoriteValidationException("You must be authenticated to view favorites");
        }
        return owner;
    }

}
