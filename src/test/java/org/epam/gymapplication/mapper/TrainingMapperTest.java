package org.epam.gymapplication.mapper;

import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.repository.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TrainingMapperTest {
    @InjectMocks
    private TrainingMapper trainingMapper;

    @Mock
    private Training training;

    @Mock
    private User traineeUser;

    @Mock
    private User trainerUser;

    @Mock
    private Trainee trainee;

    @Mock
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(training.getTrainee()).thenReturn(trainee);
        when(trainee.getUser()).thenReturn(traineeUser);
        when(traineeUser.getUsername()).thenReturn("traineeUser");

        when(training.getTrainer()).thenReturn(trainer);
        when(trainer.getUser()).thenReturn(trainerUser);
        when(trainerUser.getUsername()).thenReturn("trainerUser");

        when(training.getTrainingName()).thenReturn("Training Name");
        when(training.getTrainingDuration()).thenReturn(60.0);
        when(training.getTrainingDate()).thenReturn(new Date());
    }

    @Test
    void testMapTrainingDTOFromTrainingPayload() {
        TrainingPayload trainingPayload = TrainingPayload.builder()
                .traineeUsername("traineeUser")
                .trainerUsername("trainerUser")
                .trainingDuration(60)
                .trainingName("Training Name")
                .trainingDate(new Date())
                .build();

        TrainingDTO expectedDTO = TrainingDTO.builder()
                .traineeUsername("traineeUser")
                .trainerUsername("trainerUser")
                .trainingDuration(60)
                .trainingName("Training Name")
                .trainingDate(new Date())
                .build();
        TrainingDTO result = trainingMapper.mapTrainingDTOFromTrainingPayload(trainingPayload);

        assertEquals(expectedDTO.getTraineeUsername(), result.getTraineeUsername());
        assertEquals(expectedDTO.getTrainerUsername(), result.getTrainerUsername());
        assertEquals(expectedDTO.getTrainingDuration(), result.getTrainingDuration());
        assertEquals(expectedDTO.getTrainingName(), result.getTrainingName());
    }

    @Test
    void testMapTrainingToTrainingPayload() {
        TrainingPayload expectedPayload = TrainingPayload.builder()
                .traineeUsername("traineeUser")
                .trainerUsername("trainerUser")
                .trainingName("Training Name")
                .trainingDuration(60)
                .trainingDate(new Date())
                .build();
        TrainingPayload result = trainingMapper.mapTrainingToTrainingPayload(training);

        assertEquals(expectedPayload.getTraineeUsername(), result.getTraineeUsername());
        assertEquals(expectedPayload.getTrainerUsername(), result.getTrainerUsername());
        assertEquals(expectedPayload.getTrainingDuration(), result.getTrainingDuration());
        assertEquals(expectedPayload.getTrainingName(), result.getTrainingName());
    }
}
