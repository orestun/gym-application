package org.epam.gymapplication.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.epam.gymapplication.domain.model.TrainingType;

@Builder
@Getter
public class TrainerBasicProfileDTO {
    @NotNull(message = "Username can`t be null")
    @NotBlank(message = "Username can`t be blank")
    private String username;

    @NotNull(message = "Firth name can`t be null")
    @NotBlank(message = "Firth name can`t be blank")
    private String firstName;

    @NotNull(message = "Second name can`t be null")
    @NotBlank(message = "second name can`t be blank")
    private String lastName;

    private TrainingType trainingType;

    @NotNull(message = "Active status can`t be null")
    private Boolean isActive;
}
