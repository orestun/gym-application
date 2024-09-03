package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.domain.dao.TraineeDAO;
import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.dao.TrainingDAO;
import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainingDAOImpl;
import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.repository.mapper.TrainingMapper;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private final TrainingMapper trainingMapper;

    public TrainingService(TrainingDAOImpl trainingDAO, TrainerDAOImpl trainerDAO, TraineeDAOImpl traineeDAO, TrainingMapper trainingMapper) {
        this.trainingDAO = trainingDAO;
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
        this.trainingMapper = trainingMapper;
    }

    public void addTraining(TrainingDTO trainingDTO){
        Trainer trainer = trainerDAO
                .getTrainerByUsername(trainingDTO.getTrainerUsername())
                .orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.trainerNotFoundByUsername(trainingDTO.getTrainerUsername())));

        Trainee trainee = Optional.of(traineeDAO
                .getTraineeByUsername(trainingDTO.getTraineeUsername()))
                .orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.traineeNotFoundByUsername(trainingDTO.getTraineeUsername())));

        Training training = Training
                .builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainer.getSpecialization())
                .trainingName(trainingDTO.getTrainingName())
                .trainingDate(trainingDTO.getTrainingDate())
                .trainingDuration(trainingDTO.getTrainingDuration())
                .build();

        trainingDAO.addTraining(training);
    }

    public Set<TrainingType> getAllTrainingTypes() {
        return trainingDAO.getAllTrainingTypes();
    }

    public List<TrainingPayload> getTraineeTrainingList(TraineeTrainingCriteriaDTO criteriaDTO) {
        List<Training> trainingList = trainingDAO.getTraineeTrainingByCriteria(criteriaDTO);
        return trainingList.stream()
                .map(trainingMapper::mapTrainingToTrainingPayload)
                .toList();
    }

    public List<TrainingPayload> getTrainerTrainingList(TrainerTrainingCriteriaDTO criteriaDTO) {
        List<Training> trainingList = trainingDAO.getTrainerTrainingByCriteria(criteriaDTO);
        return trainingList.stream()
                .map(trainingMapper::mapTrainingToTrainingPayload)
                .toList();
    }
}
