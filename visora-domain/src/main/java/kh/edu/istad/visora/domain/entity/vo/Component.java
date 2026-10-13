package kh.edu.istad.visora.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Component {

    private long id;

    private String uuid;

    private String type;

    private String content;

    private String shape;

    private Position position;

    private Size size;

    private Double rotation;

    private Integer layerIndex;

    private boolean locked;

    private boolean visible;

    private String image;

    private Object vector;

    private String name;

    private Size lockAspect;

    private String groupUuid;

    private boolean flipX;

    private boolean flipY;

    private String morphId;

    private String pageNumber;

    private Map<String, Object> animation;

    private Map<String, Object> styles;

    private Map<String, Object> timer;
}