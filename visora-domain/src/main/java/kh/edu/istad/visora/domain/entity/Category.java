package kh.edu.istad.visora.domain.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category extends Entity {

    private long id;

    private String uuid;

    private String name;

    private String slug;

    private String description;

    private String icon;

    private Integer displayOrder = 0;

    @ToString.Exclude
    private List<Template> templates;

    private long templateCount;

    private boolean isActive;

}
