package kh.edu.istad.visora.domain.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Role extends Entity {

    private long id;

    private String uuid;

    private String role;

    private String description;

}
