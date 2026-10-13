package kh.edu.istad.visora.data.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimationEntity {

    private String id;

    private String elementId;

    private String kind;

    private String preset;

    private String trigger;

    private double delayMs;

    private double durationMs;

}