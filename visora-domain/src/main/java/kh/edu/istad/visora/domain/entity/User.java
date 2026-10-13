package kh.edu.istad.visora.domain.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class User extends Entity {

    private long id;

    private String uuid;

    private String username;

    private String familyName;

    private String givenName;

    private String gender;

    private String birthdate;

    private String picture;

    private String phoneNumber;

    private String email;

    private boolean emailVerified;

    private String password;

    private Role role;

    @ToString.Exclude
    private List<Favorite> favorites;

}
