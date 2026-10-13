package kh.edu.istad.visora.data.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransitionEntity {

    private String preset;

    private double durationMs;

    private double delayMs;

}