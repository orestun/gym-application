package org.epam.gymapplication.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.epam.gymapplication.domain.dao.impl.TrainingDAOImpl;
import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingDAOTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Training> criteriaQuery;

    @Mock
    private Root<Training> root;

    @Mock
    private List<Predicate> predicates;
    @Mock
    private Join<Object, Object> traineeTrainingJoin;

    @Mock
    private Join<Object, Object> trainerTrainingJoin;

    @Mock
    private Join<Object, Object> traineeUserJoin;

    @Mock
    private Join<Object, Object> trainerUserJoin;

    @Mock
    private Join<Object, Object> trainingTypeJoin;

    @Mock
    private TypedQuery<Training> typedQuery;

    @InjectMocks
    private TrainingDAOImpl trainingDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(root.join("trainee")).thenReturn(traineeTrainingJoin);
        when(root.join("trainer")).thenReturn(trainerTrainingJoin);
        when(traineeTrainingJoin.join("user")).thenReturn(traineeUserJoin);
        when(trainerTrainingJoin.join("user")).thenReturn(trainerUserJoin);
        when(root.join("trainingType")).thenReturn(trainingTypeJoin);
    }

    @Test
    void testAddTraining() {
        Training training = new Training();
        trainingDAO.addTraining(training);
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    void testGetAllTrainingTypes() {
        Set<TrainingType> expectedTypes = new HashSet<>();
        when(trainingRepository.getAllTrainingTypes()).thenReturn(expectedTypes);

        Set<TrainingType> result = trainingDAO.getAllTrainingTypes();
        assertEquals(expectedTypes, result);
    }

    @Test
    void testGetTraineeTrainingByCriteria() {
        TraineeTrainingCriteriaDTO criteria = TraineeTrainingCriteriaDTO.builder().build();
        List<Training> expectedTrainings = new ArrayList<>();
        when(typedQuery.getResultList()).thenReturn(expectedTrainings);
        List<Training> result = trainingDAO.getTraineeTrainingByCriteria(criteria);

        assertEquals(expectedTrainings, result);
    }

    @Test
    void testGetTrainerTrainingByCriteria() {
        TrainerTrainingCriteriaDTO criteria = TrainerTrainingCriteriaDTO.builder().build();
        List<Training> expectedTrainings = new ArrayList<>();
        when(typedQuery.getResultList()).thenReturn(expectedTrainings);

        List<Training> result = trainingDAO.getTrainerTrainingByCriteria(criteria);

        assertEquals(expectedTrainings, result);
    }
}
