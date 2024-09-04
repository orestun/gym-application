package org.epam.gymapplication.domain.dao.impl;

import org.epam.gymapplication.domain.dao.TraineeDAO;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.repository.TraineeRepository;
import org.epam.gymapplication.repository.TrainingRepository;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;

    public TraineeDAOImpl(TraineeRepository traineeRepository, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
    }


    @Override
    public Trainee getTraineeByUsername(String username) {
        return traineeRepository
                .findByUser_Username(username)
                .orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.traineeNotFoundByUsername(username)));
    }

    @Override
    public Trainee addTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    @Transactional
    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    @Transactional
    @Override
    public void deleteTraineeProfileByUsername(String username) {
        Trainee trainee = traineeRepository
                .findByUser_Username(username)
                .orElseThrow(
                        () -> new ItemNotExistsException(ExceptionMessage.traineeNotFoundByUsername(username)));
        traineeRepository.clearTrainersAssociationsFromTraineeByUsername(trainee.getId());
        trainingRepository.deleteByTrainee_Id(trainee.getId());

        traineeRepository.deleteByUserUsername(username);
    }

    @Override
    public void updateActiveStatus(String username, boolean active) {
        traineeRepository.updateActiveStatusByUsername(username, active);
    }

    @Override
    public List<String> findAllTraineeUsernames_ByPrefix(String prefix) {
        return traineeRepository.findAllUsernames_ByPrefix(prefix);
    }

    @Override
    public boolean existsTraineeByUsername(String username) {
        return traineeRepository.existsByUser_Username(username);
    }
}
