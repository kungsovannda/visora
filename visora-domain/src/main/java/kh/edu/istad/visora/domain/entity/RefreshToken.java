package kh.edu.istad.visora.domain.entity;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshToken extends Entity {

    private long id;

    private String jti;

    private String userUuid;

    private String tokenHash;

    private Timestamp expiresAt;

    private boolean revoked;

    private Timestamp revokedAt;

    private String replacedByJti;

}