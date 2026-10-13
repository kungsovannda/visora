package kh.edu.istad.visora.domain.ports.inbound.usecase.favorite;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteNotFoundException;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteValidationException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteFavoriteUseCase {

    private final FavoriteRepository favoriteRepository;
    private final CurrentUser currentUser;

    public void delete(String uuid) {
        User owner = requireCurrentUser();
        var favorite = favoriteRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite was not found"));
        favoriteRepository.delete(favorite);
    }

    private User requireCurrentUser() {
        User owner = currentUser.get();
        if (owner == null) {
            throw new FavoriteValidationException("You must be authenticated to manage favorites");
        }
        return owner;
    }

}
