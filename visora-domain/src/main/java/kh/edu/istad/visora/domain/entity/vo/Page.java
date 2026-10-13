package kh.edu.istad.visora.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private long id;

    private String uuid;

    private String name;

    private int pageNumber;

    private List<Group> groups;

    private Map<String, Object> background;

    private List<Animation> animations;

    private Transition transition;

    private List<Component> components;
}