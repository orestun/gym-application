package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TraineeBasicProfileDTO;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;

import java.util.List;

public interface ITraineeService {
    Trainee getTraineeByUsername(String username);
    Trainee addTrainee(AuthDTO authDTO, TraineeBasicProfileDTO traineeDTO);
    Trainee updateTrainee(TraineeBasicProfileDTO traineeDTO);
    void deleteTraineeProfileByUsername(String username);
    void updateActiveStatusByUsername(String username, boolean isActive);
    List<Trainer> updateTrainerListForTrainee_ByUsername(String traineeUsername, List<String> trainerUsernameList);

}
