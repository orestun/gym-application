package org.epam.gymapplication.domain.dao;

import org.epam.gymapplication.domain.model.Trainee;

import java.util.List;

public interface TraineeDAO {
    Trainee getTraineeByUsername(String username);
    Trainee addTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTraineeProfileByUsername(String username);
    void updateActiveStatus(String username, boolean active);
    List<String> findAllTraineeUsernames_ByPrefix(String prefix);
    boolean existsTraineeByUsername(String username);
}
