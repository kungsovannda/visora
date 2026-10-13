package kh.edu.istad.visora.domain.entity;

import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.entity.vo.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Backdrop extends Entity {

    private long id;

    private String uuid;

    private int clientSchemaVersion;

    private String name;

    private User owner;

    private String sourceTemplateUuid;

    private String thumbnail;

    private Orientation orientation;

    private Size canvas;

    private List<Page> pages;

    private int pageCount;

    private boolean hasTimer;

    private boolean canExport;

    private Status status;

    private long version;

    private Timestamp lastOpenedAt;

    private Timestamp lastSavedAt;

    private Map<String, Object> settings;

    public enum Orientation {
        LANDSCAPE,
        PORTRAIT
    }

    public enum Status {
        ACTIVE,
        ARCHIVED
    }
}