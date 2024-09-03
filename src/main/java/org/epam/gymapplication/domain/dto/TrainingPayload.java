package org.epam.gymapplication.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Builder
@Setter
public class TrainingPayload {

    @NotNull(message = "Trainee username can`t be null (it`s required field)")
    @NotNull(message = "Trainee username can`t be blank")
    private String traineeUsername;

    @NotNull(message = "Trainer username can`t be null (it`s required field)")
    @NotNull(message = "Trainer username can`t be blank")
    private String trainerUsername;

    @NotNull(message = "Training name can`t be null (it`s required field)")
    @NotNull(message = "Training name can`t be blank")
    private String trainingName;

    @NotNull(message = "Training date can`t be null (it`s required field)")
    private Date trainingDate;

    @NotNull(message = "Training duration can`t be null (it`s required field)")
    @Min(value = 0, message = "Training duration should be non negative number")
    private double trainingDuration;
}
