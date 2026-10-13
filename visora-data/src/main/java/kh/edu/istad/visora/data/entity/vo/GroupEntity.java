package kh.edu.istad.visora.data.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {

    private String uuid;

    private String name;

    private boolean visible;

    private boolean locked;

}