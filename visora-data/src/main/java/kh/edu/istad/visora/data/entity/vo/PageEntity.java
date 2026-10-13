package kh.edu.istad.visora.data.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageEntity {

    private long id;

    private String uuid;

    private String name;

    private int pageNumber;

    private List<GroupEntity> groups;

    private Map<String, Object> background;

    private List<AnimationEntity> animations;

    private TransitionEntity transition;

    private List<ComponentEntity> components;

}