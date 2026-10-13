package kh.edu.istad.visora.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storages")
@Entity
public class StorageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_name", unique = true, nullable = false)
    private String fileName;

    private String extension;

    @Column(name = "mime_type")
    private String mimeType;

    private String path;

}