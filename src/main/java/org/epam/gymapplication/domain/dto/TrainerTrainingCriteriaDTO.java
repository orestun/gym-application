package org.epam.gymapplication.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class TrainerTrainingCriteriaDTO {
    private String username;
    private Date periodFrom;
    private Date periodTo;
    private String traineeName;
}
