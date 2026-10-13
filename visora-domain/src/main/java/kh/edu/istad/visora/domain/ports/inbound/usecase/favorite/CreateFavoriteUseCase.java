package kh.edu.istad.visora.domain.ports.inbound.usecase.favorite;

import kh.edu.istad.visora.domain.entity.Favorite;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.entity.enums.MainType;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteAlreadyExistException;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteValidationException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.exception.template.TemplateNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.FavoriteRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateFavoriteUseCase {

    private final FavoriteRepository favoriteRepository;
    private final TemplateRepository templateRepository;
    private final BackdropRepository backdropRepository;
    private final CurrentUser currentUser;

    public Favorite create(String templateUuid, String backdropUuid) {
        boolean hasTemplate = templateUuid != null && !templateUuid.isBlank();
        boolean hasBackdrop = backdropUuid != null && !backdropUuid.isBlank();

        if (hasTemplate == hasBackdrop) {
            throw new FavoriteValidationException(
                    "A favorite must reference exactly one target: provide either a templateUuid or a backdropUuid, not both"
            );
        }

        User owner = requireCurrentUser();
        return hasTemplate
                ? favoriteTemplate(owner, templateUuid)
                : favoriteBackdrop(owner, backdropUuid);
    }

    private Favorite favoriteTemplate(User owner, String templateUuid) {
        var template = templateRepository.findByUuid(templateUuid)
                .orElseThrow(() -> new TemplateNotFoundException("Template was not found"));

        favoriteRepository.findByOwnerAndTarget(owner, MainType.TEMPLATE, template.getUuid())
                .ifPresent(existing -> {
                    throw new FavoriteAlreadyExistException("This template is already in your favorites");
                });

        var favorite = Favorite.ofTemplate(owner, template);
        return favoriteRepository.save(prepare(favorite));
    }

    private Favorite favoriteBackdrop(User owner, String backdropUuid) {
        var backdrop = backdropRepository.findByUuid(backdropUuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));

        favoriteRepository.findByOwnerAndTarget(owner, MainType.BACKDROP, backdrop.getUuid())
                .ifPresent(existing -> {
                    throw new FavoriteAlreadyExistException("This backdrop is already in your favorites");
                });

        var favorite = Favorite.ofBackdrop(owner, backdrop);
        return favoriteRepository.save(prepare(favorite));
    }

    private Favorite prepare(Favorite favorite) {
        favorite.setId(0);
        favorite.setUuid(UUID.randomUUID().toString());
        favorite.validate();
        return favorite;
    }

    private User requireCurrentUser() {
        User owner = currentUser.get();
        if (owner == null) {
            throw new FavoriteValidationException("You must be authenticated to manage favorites");
        }
        return owner;
    }

}
