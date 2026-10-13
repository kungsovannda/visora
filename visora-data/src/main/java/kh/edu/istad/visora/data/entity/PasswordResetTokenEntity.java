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
@Table(name = "password_reset_tokens")
@Entity
public class PasswordResetTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userUuid;

    @Column(unique = true, nullable = false)
    private String tokenHash;

    private Timestamp expiresAt;

    @Column(nullable = false)
    private boolean used;

}