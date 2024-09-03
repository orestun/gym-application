package org.epam.gymapplication.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrainingDTO {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Date trainingDate;
    private double trainingDuration;
}
