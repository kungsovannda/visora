package kh.edu.istad.visora.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
@Entity
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String jti;

    @Column(nullable = false)
    private String userUuid;

    private Timestamp expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    private Timestamp revokedAt;

    private String replacedByJti;

}