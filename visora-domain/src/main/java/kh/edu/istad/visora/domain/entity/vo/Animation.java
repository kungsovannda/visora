package kh.edu.istad.visora.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animation {

    private String id;

    private String elementId;

    private String kind;

    private String preset;

    private String trigger;

    private double delayMs;

    private double durationMs;

}