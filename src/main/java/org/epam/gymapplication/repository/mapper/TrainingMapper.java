package org.epam.gymapplication.repository.mapper;

import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.domain.model.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {
    public TrainingDTO mapTrainingDTOFromTrainingPayload(TrainingPayload trainingPayload) {
        return TrainingDTO.builder()
                .traineeUsername(trainingPayload.getTraineeUsername())
                .trainerUsername(trainingPayload.getTrainerUsername())
                .trainingDuration(trainingPayload.getTrainingDuration())
                .trainingName(trainingPayload.getTrainingName())
                .trainingDate(trainingPayload.getTrainingDate())
                .build();
    }

    public TrainingPayload mapTrainingToTrainingPayload(Training training) {
        return TrainingPayload.builder()
                .traineeUsername(training.getTrainee().getUser().getUsername())
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .trainingName(training.getTrainingName())
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();
    }
}
