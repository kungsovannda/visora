package kh.edu.istad.visora.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kh.edu.istad.visora.data.entity.vo.PageEntity;
import kh.edu.istad.visora.data.entity.vo.SizeEntity;
import kh.edu.istad.visora.domain.entity.Backdrop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "backdrops")
@Entity
public class BackdropEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(name = "client_schema_version")
    private int clientSchemaVersion;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(name = "source_template_uuid")
    private String sourceTemplateUuid;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private Backdrop.Orientation orientation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private SizeEntity canvas;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<PageEntity> pages;

    @Column(name = "page_count")
    private int pageCount;

    @Column(name = "has_timer")
    private boolean hasTimer;

    @Column(name = "can_export")
    private boolean canExport;

    @Enumerated(EnumType.STRING)
    private Backdrop.Status status;

    private long version;

    @Column(name = "last_opened_at")
    private Timestamp lastOpenedAt;

    @Column(name = "last_saved_at")
    private Timestamp lastSavedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> settings;

}