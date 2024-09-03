package org.epam.gymapplication.service;


import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TrainerBasicProfileDTO;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.service.impl.TrainerService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainerServiceTest {

    @Mock
    TrainerDAO trainerDAO;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    TrainerService trainerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTrainerByUsernameTest(){
        String testUsername = "testUsername";
        Mockito.when(trainerDAO.getTrainerByUsername(testUsername))
                .thenReturn(Optional.ofNullable(
                        Trainer.builder().user(User.builder().username(testUsername).build()).build()));
        Trainer result = trainerService.getTrainerByUsername(testUsername);
        assertEquals(result.getUser().getUsername(), testUsername);
    }

    @Test
    public void getTrainerByUsername_ItemNotExistsTest(){
        String testUsername = "testUsername";
        Mockito.when(trainerDAO.getTrainerByUsername(testUsername))
                .thenReturn(Optional.empty());

        ItemNotExistsException e = assertThrows(ItemNotExistsException.class, () -> trainerService.getTrainerByUsername(testUsername));
        assertEquals(e.getMessage(), ExceptionMessage.trainerNotFoundByUsername(testUsername));
    }

    @Test
    public void addTrainerTest(){
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        TrainerBasicProfileDTO trainerDTO = TrainerBasicProfileDTO.builder().firstName("John").lastName("Doe").isActive(true).trainingType(new TrainingType()).build();
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();

        Trainer trainer = Trainer.builder().user(user).build();
        Mockito.when(passwordEncoder.encode(authDTO.getPassword())).thenReturn("encodedpassword");
        Mockito.when(trainerDAO.addTrainer(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.addTrainer(authDTO, trainerDTO, new TrainingType());

        assertEquals(trainer, result);
        verify(passwordEncoder, times(1)).encode(authDTO.getPassword());
        verify(trainerDAO, times(1)).addTrainer(any(Trainer.class));
    }

    @Test
    public void updateTrainerTest(){
        TrainerBasicProfileDTO trainerDTO = TrainerBasicProfileDTO.builder().username("testuser").firstName("John").lastName("Doe").isActive(true).trainingType(new TrainingType()).build();

        Mockito.when(trainerDAO.getTrainerByUsername(trainerDTO.getUsername()))
                .thenReturn(Optional.of(Trainer.builder().user(new User()).build()));
        Mockito.when(trainerDAO.updateTrainer(any(Trainer.class)))
                .thenReturn(Trainer.builder().user(User.builder().username("testuser").build()).build());

        assertEquals(trainerDTO.getUsername(), trainerService.updateTrainer(trainerDTO).getUser().getUsername());
    }

    @Test
    void testUpdateActiveStatusByUsername() {
        String username = "testUser";
        boolean isActive = true;
        trainerService.updateActiveStatusByUsername(username, isActive);
        Mockito.verify(trainerDAO, Mockito.times(1)).updateActiveStatus(username, isActive);
    }

    @Test
    void testFindAllTrainersNotAssignedToTrainee_ByUsername() {
        String username = "traineeUser";
        List<Trainer> mockTrainers = Arrays.asList(
                Trainer.builder().user(User.builder().username("trainer1").build()).build(),
                Trainer.builder().user(User.builder().username("trainer2").build()).build()
        );
        Mockito.when(trainerDAO.findAllTrainersNotAssignedToTrainee_ByUsername(username))
                .thenReturn(mockTrainers);
        List<Trainer> result = trainerService.findAllTrainersNotAssignedToTrainee_ByUsername(username);

        assertEquals(mockTrainers.size(), result.size());
        assertEquals(mockTrainers, result);
        Mockito.verify(trainerDAO, Mockito.times(1)).findAllTrainersNotAssignedToTrainee_ByUsername(username);
    }

}
