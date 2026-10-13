package kh.edu.istad.visora.domain.entity;

import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.entity.vo.Size;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Template extends Entity {

    private long id;

    private String uuid;

    private int clientSchemaVersion;

    private String name;

    private User owner;

    private Category category;

    private String sourceTemplateUuid;

    private String sourceBackdropUuid;

    private String thumbnail;

    private Backdrop.Orientation orientation;

    private Size canvas;

    private List<Page> pages;

    private int pageCount;

    private boolean hasTimer;

    private boolean canExport;

    private Backdrop.Status status;

    private TemplateStatus templateStatus;

    private String proposedName;

    private String description;

    private String eventType;

    private List<String> styles;

    private List<String> colors;

    private User submittedBy;

    private Timestamp submittedAt;

    private long version;

    private Timestamp lastOpenedAt;

    private Timestamp lastSavedAt;

    private Map<String, Object> settings;

    public String getCategoryUuid() {
        return category == null ? null : category.getUuid();
    }

}
