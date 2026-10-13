package kh.edu.istad.visora.data.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComponentEntity {

    private long id;

    private String uuid;

    private String type;

    private String content;

    private String shape;

    private PositionEntity position;

    private SizeEntity size;

    private Double rotation;

    private Integer layerIndex;

    private boolean locked;

    private boolean visible;

    private String image;

    private Object vector;

    private String name;

    private SizeEntity lockAspect;

    private String groupUuid;

    private boolean flipX;

    private boolean flipY;

    private String morphId;

    private String pageNumber;

    private Map<String, Object> animation;

    private Map<String, Object> styles;

    private Map<String, Object> timer;

}