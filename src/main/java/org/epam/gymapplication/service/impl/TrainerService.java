package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TrainerBasicProfileDTO;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.service.ITrainerService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService implements ITrainerService {
    private final TrainerDAO trainerDAO;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(TrainerDAO trainerDAO, PasswordEncoder passwordEncoder) {
        this.trainerDAO = trainerDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Trainer getTrainerByUsername(String username) {
        return trainerDAO
                .getTrainerByUsername(username)
                .orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.trainerNotFoundByUsername(username)));
    }

    public Trainer addTrainer(AuthDTO authDTO, TrainerBasicProfileDTO trainerDTO, TrainingType specialization) {
        User user = User.builder()
                .firthName(trainerDTO.getFirstName())
                .secondName(trainerDTO.getLastName())
                .username(authDTO.getUsername())
                .isActive(true)
                .password(passwordEncoder.encode(authDTO.getPassword())).build();

        Trainer trainer = Trainer.builder()
                .specialization(specialization)
                .user(user).build();

        return trainerDAO.addTrainer(trainer);
    }

    public Trainer updateTrainer(TrainerBasicProfileDTO trainerDTO) {
        Trainer trainer = getTrainerByUsername(trainerDTO.getUsername());

        User user = trainer.getUser();
        user.setFirthName(trainerDTO.getFirstName());
        user.setSecondName(trainerDTO.getLastName());
        user.setActive(trainerDTO.getIsActive());

        trainer.setSpecialization(trainerDTO.getTrainingType());
        trainer.setUser(user);

        return trainerDAO.updateTrainer(trainer);
    }

    public void updateActiveStatusByUsername(String username, boolean isActive) {
        trainerDAO.updateActiveStatus(username, isActive);
    }

    public List<Trainer> findAllTrainersNotAssignedToTrainee_ByUsername(String username) {
        return trainerDAO.findAllTrainersNotAssignedToTrainee_ByUsername(username);
    }

}


