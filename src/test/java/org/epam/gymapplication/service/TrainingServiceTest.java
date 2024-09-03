package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainingDAOImpl;
import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.domain.model.*;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.repository.mapper.TrainingMapper;
import org.epam.gymapplication.service.impl.TrainingService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @Mock
    TrainingDAOImpl trainingDAO;
    @Mock
    TrainerDAOImpl trainerDAO;
    @Mock
    TraineeDAOImpl traineeDAO;
    @Mock
    TrainingMapper trainingMapper;
    @InjectMocks
    TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTraining_Success() {
        String trainerUsername = "trainer1";
        String traineeUsername = "trainee1";

        Trainer trainer = Trainer.builder()
                .user(User.builder().username(trainerUsername).build())
                .specialization(new TrainingType())
                .build();

        Trainee trainee = Trainee.builder()
                .user(User.builder().username(traineeUsername).build())
                .build();

        Mockito.when(trainerDAO.getTrainerByUsername(trainerUsername)).thenReturn(Optional.of(trainer));
        Mockito.when(traineeDAO.getTraineeByUsername(traineeUsername)).thenReturn(trainee);

        TrainingDTO trainingDTO = TrainingDTO.builder().trainerUsername(trainerUsername).traineeUsername(traineeUsername).trainingName("Training name").trainingDate(new Date()).trainingDuration(60).build();
        trainingService.addTraining(trainingDTO);

        verify(trainingDAO).addTraining(any(Training.class));
    }

    @Test
    void testAddTraining_TrainerNotFound() {
        String trainerUsername = "trainer1";
        String traineeUsername = "trainee1";
        TrainingDTO trainingDTO = TrainingDTO.builder().trainerUsername(trainerUsername).traineeUsername(traineeUsername).trainingName("Training name").trainingDate(new Date()).trainingDuration(60).build();

        Mockito.when(trainerDAO.getTrainerByUsername(trainerUsername)).thenReturn(Optional.empty());

        ItemNotExistsException exception = assertThrows(ItemNotExistsException.class, () ->
                trainingService.addTraining(trainingDTO)
        );

        assertEquals(ExceptionMessage.trainerNotFoundByUsername(trainerUsername), exception.getMessage());
        verify(trainingDAO, never()).addTraining(any(Training.class));
    }

    @Test
    void testGetAllTrainingTypes() {
        Set<TrainingType> expectedTypes = new HashSet<>();
        expectedTypes.add(new TrainingType(0L, "Type1"));
        expectedTypes.add(new TrainingType(1L,"Type2"));

        Mockito.when(trainingDAO.getAllTrainingTypes()).thenReturn(expectedTypes);
        Set<TrainingType> actualTypes = trainingService.getAllTrainingTypes();

        assertEquals(expectedTypes, actualTypes);
        verify(trainingDAO, times(1)).getAllTrainingTypes();
    }

    @Test
    void testGetTraineeTrainingList() {
        TraineeTrainingCriteriaDTO criteriaDTO = TraineeTrainingCriteriaDTO.builder().build();
        List<Training> trainingList = new ArrayList<>();
        trainingList.add(new Training());

        TrainingPayload trainingPayload = TrainingPayload.builder().build();

        when(trainingDAO.getTraineeTrainingByCriteria(criteriaDTO)).thenReturn(trainingList);
        when(trainingMapper.mapTrainingToTrainingPayload(any(Training.class))).thenReturn(trainingPayload);

        List<TrainingPayload> result = trainingService.getTraineeTrainingList(criteriaDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainingPayload, result.get(0));
        verify(trainingDAO, times(1)).getTraineeTrainingByCriteria(criteriaDTO);
        verify(trainingMapper, times(1)).mapTrainingToTrainingPayload(any(Training.class));
    }

    @Test
    void testGetTrainerTrainingList() {
        TrainerTrainingCriteriaDTO criteriaDTO = TrainerTrainingCriteriaDTO.builder().build();
        List<Training> trainingList = new ArrayList<>();
        trainingList.add(new Training());

        TrainingPayload trainingPayload = TrainingPayload.builder().build();

        when(trainingDAO.getTrainerTrainingByCriteria(criteriaDTO)).thenReturn(trainingList);
        when(trainingMapper.mapTrainingToTrainingPayload(any(Training.class))).thenReturn(trainingPayload);

        List<TrainingPayload> result = trainingService.getTrainerTrainingList(criteriaDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainingPayload, result.get(0));
        verify(trainingDAO, times(1)).getTrainerTrainingByCriteria(criteriaDTO);
        verify(trainingMapper, times(1)).mapTrainingToTrainingPayload(any(Training.class));
    }
}
