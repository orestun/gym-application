package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.domain.model.TrainingType;

import java.util.List;
import java.util.Set;

public interface ITrainingService {
    void addTraining(TrainingDTO trainingDTO);
    Set<TrainingType> getAllTrainingTypes();
    List<TrainingPayload> getTraineeTrainingList(TraineeTrainingCriteriaDTO criteriaDTO);
    List<TrainingPayload> getTrainerTrainingList(TrainerTrainingCriteriaDTO criteriaDTO);
}
