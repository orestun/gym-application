package org.epam.gymapplication.domain.dao;

import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.TrainingType;

import java.util.List;
import java.util.Set;

public interface TrainingDAO {
    void addTraining(Training training);
    Set<TrainingType> getAllTrainingTypes();
    List<Training> getTraineeTrainingByCriteria(TraineeTrainingCriteriaDTO criteria);
    List<Training> getTrainerTrainingByCriteria(TrainerTrainingCriteriaDTO criteria);
}
