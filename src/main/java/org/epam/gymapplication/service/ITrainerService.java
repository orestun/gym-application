package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TrainerBasicProfileDTO;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;

import java.util.List;

public interface ITrainerService {
    Trainer getTrainerByUsername(String username);
    Trainer addTrainer(AuthDTO authDTO, TrainerBasicProfileDTO trainerDTO, TrainingType specialization);
    Trainer updateTrainer(TrainerBasicProfileDTO trainerDTO);
    void updateActiveStatusByUsername(String username, boolean isActive);
    List<Trainer> findAllTrainersNotAssignedToTrainee_ByUsername(String username);
}
