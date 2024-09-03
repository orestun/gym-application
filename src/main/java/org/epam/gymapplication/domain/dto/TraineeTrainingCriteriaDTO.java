package org.epam.gymapplication.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class TraineeTrainingCriteriaDTO {
    private String username;
    private Date periodFrom;
    private Date periodTo;
    private String trainerName;
    private String trainingType;
}
