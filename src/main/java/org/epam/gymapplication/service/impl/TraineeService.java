package org.epam.gymapplication.service.impl;

import jakarta.transaction.Transactional;
import org.epam.gymapplication.domain.dao.TraineeDAO;
import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TraineeBasicProfileDTO;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final PasswordEncoder passwordEncoder;

    public TraineeService(TraineeDAOImpl traineeDAO, TrainerDAOImpl trainerDAO, PasswordEncoder passwordEncoder) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Trainee getTraineeByUsername(String username){
        return traineeDAO.getTraineeByUsername(username);
    }

    public Trainee addTrainee(AuthDTO authDTO, TraineeBasicProfileDTO traineeDTO){
        User user = User.builder()
                .firthName(traineeDTO.getFirstName())
                .secondName(traineeDTO.getLastName())
                .username(authDTO.getUsername())
                .password(passwordEncoder.encode(authDTO.getPassword()))
                .isActive(true).build();

        Trainee trainee = Trainee.builder().user(user).build();

        if(traineeDTO.getDateOfBirth() != null){
            trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        }
        if (traineeDTO.getAddress() != null){
            trainee.setAddress(traineeDTO.getAddress());
        }
        return traineeDAO.addTrainee(trainee);
    }

    public Trainee updateTrainee(TraineeBasicProfileDTO traineeDTO){
        Trainee trainee = traineeDAO.getTraineeByUsername(traineeDTO.getUsername());
        trainee.getUser().setFirthName(traineeDTO.getFirstName());
        trainee.getUser().setSecondName(traineeDTO.getLastName());
        trainee.getUser().setActive(traineeDTO.isActive());
        if(traineeDTO.getDateOfBirth() != null){
            trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        }
        if(traineeDTO.getAddress() != null){
            trainee.setAddress(traineeDTO.getAddress());
        }
        trainee.getUser().setActive(traineeDTO.isActive());
        return traineeDAO.updateTrainee(trainee);
    }

    @Transactional
    public void deleteTraineeProfileByUsername(String username){
        traineeDAO.deleteTraineeProfileByUsername(username);
    }

    public void updateActiveStatusByUsername(String username, boolean isActive){
        traineeDAO.updateActiveStatus(username, isActive);
    }

    public List<Trainer> updateTrainerListForTrainee_ByUsername(String traineeUsername, List<String> trainerUsernameList) {
        Trainee trainee = traineeDAO
                .getTraineeByUsername(traineeUsername);
        Set<Trainer> trainerSet = getTrainerSetForTrainee_ByTrainersUsernamesList(trainerUsernameList, trainee);
        trainee.setTrainers(trainerSet);
        return traineeDAO.updateTrainee(trainee)
                .getTrainers()
                .stream()
                .toList();
    }

    private Set<Trainer> getTrainerSetForTrainee_ByTrainersUsernamesList(List<String> trainerUsernamesList, Trainee trainee){
        return mapTrainersUsernamesIntoTrainersEntity(trainerUsernamesList)
                .stream()
                .map(t -> addNewTraineeEntityIntoTrainer(t, trainee))
                .collect(Collectors.toSet());
    }

    private Set<Trainer> mapTrainersUsernamesIntoTrainersEntity(List<String> trainerUsernamesList){
        return trainerUsernamesList
                .stream()
                .map(username -> trainerDAO.getTrainerByUsername(username)
                        .orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.trainerNotFoundByUsername(username))))
                .collect(Collectors.toSet());
    }

    private Trainer addNewTraineeEntityIntoTrainer(Trainer trainer, Trainee trainee){
        Set<Trainee> traineeSet = trainer.getTrainee();
        traineeSet.add(trainee);
        trainer.setTrainee(traineeSet);
        return trainer;
    }
}
