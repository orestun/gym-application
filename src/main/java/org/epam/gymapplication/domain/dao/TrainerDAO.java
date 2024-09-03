package org.epam.gymapplication.domain.dao;

import org.epam.gymapplication.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    Optional<Trainer> getTrainerByUsername(String username);
    Trainer addTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    void updateActiveStatus(String username, boolean active);
    List<Trainer> findAllTrainersNotAssignedToTrainee_ByUsername(String username);
    List<String> findAllTrainerUsernames_ByPrefix(String prefix);
}
