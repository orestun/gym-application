package org.epam.gymapplication.domain.dao.impl;

import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    private final TrainerRepository trainerRepository;

    public TrainerDAOImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerRepository.findByUser_Username(username);
    }

    @Override
    public Trainer addTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    public void updateActiveStatus(String username, boolean active) {
        trainerRepository.updateActiveStatusByUsername(username, active);
    }

    @Override
    public List<Trainer> findAllTrainersNotAssignedToTrainee_ByUsername(String username) {
        return trainerRepository.findAllTrainersNotAssignedToTrainee_ByUsername(username);
    }

    @Override
    public List<String> findAllTrainerUsernames_ByPrefix(String prefix) {
        return trainerRepository.findAllUsernames_ByPrefix(prefix);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }
}
