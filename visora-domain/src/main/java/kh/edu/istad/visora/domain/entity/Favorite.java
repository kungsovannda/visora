package kh.edu.istad.visora.domain.entity;

import kh.edu.istad.visora.domain.entity.enums.MainType;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite extends Entity {

    private long id;

    private String uuid;

    private User owner;

    private MainType type;

    private Backdrop backdrop;

    private Template template;

    public static Favorite ofTemplate(User owner, Template template) {
        requireOwner(owner);
        if (template == null) {
            throw new FavoriteValidationException("Template must not be null when favoriting a template");
        }
        return Favorite.builder()
                .owner(owner)
                .type(MainType.TEMPLATE)
                .template(template)
                .build();
    }

    public static Favorite ofBackdrop(User owner, Backdrop backdrop) {
        requireOwner(owner);
        if (backdrop == null) {
            throw new FavoriteValidationException("Backdrop must not be null when favoriting a backdrop");
        }
        return Favorite.builder()
                .owner(owner)
                .type(MainType.BACKDROP)
                .backdrop(backdrop)
                .build();
    }

    public String getTargetUuid() {
        return targetsTemplate()
                ? (template == null ? null : template.getUuid())
                : (backdrop == null ? null : backdrop.getUuid());
    }

    public boolean targetsTemplate() {
        return type == MainType.TEMPLATE;
    }

    public boolean targetsBackdrop() {
        return type == MainType.BACKDROP;
    }

    public boolean hasSingleTarget() {
        return (template != null) ^ (backdrop != null);
    }

    public void validate() {
        if (type == null) {
            throw new FavoriteValidationException("Favorite type is required");
        }
        if (owner == null) {
            throw new FavoriteValidationException("Favorite owner is required");
        }
        if (!hasSingleTarget()) {
            throw new FavoriteValidationException(
                    "A favorite must reference exactly one target: provide either a templateUuid or a backdropUuid, not both"
            );
        }
        if (targetsTemplate() && template == null) {
            throw new FavoriteValidationException("Favorite of type TEMPLATE must reference a template");
        }
        if (targetsBackdrop() && backdrop == null) {
            throw new FavoriteValidationException("Favorite of type BACKDROP must reference a backdrop");
        }
    }

    private static void requireOwner(User owner) {
        if (owner == null) {
            throw new FavoriteValidationException("Favorite owner is required");
        }
    }

}
