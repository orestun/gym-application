package org.epam.gymapplication.dao;

import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDAOTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerDAOImpl trainerDAO;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(User.builder().username("testTrainer").password("password").build());
    }

    @Test
    public void testGetTrainerByUsername_Success() {
        String username = "testTrainer";

        Mockito.when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerDAO.getTrainerByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUser().getUsername());
    }

    @Test
    public void testGetTrainerByUsername_NotFound() {
        String username = "nonExistingTrainer";

        Mockito.when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerDAO.getTrainerByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAddTrainer() {
        Mockito.when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer result = trainerDAO.addTrainer(trainer);

        assertNotNull(result);
        assertEquals(trainer.getId(), result.getId());
    }

    @Test
    public void testUpdateActiveStatus() {
        String username = "testTrainer";
        boolean active = true;

        trainerDAO.updateActiveStatus(username, active);

        Mockito.verify(trainerRepository).updateActiveStatusByUsername(username, active);
    }

    @Test
    public void testFindAllTrainersNotAssignedToTrainee_ByUsername() {
        String traineeUsername = "testTrainee";
        List<Trainer> trainers = Collections.singletonList(trainer);

        Mockito.when(trainerRepository.findAllTrainersNotAssignedToTrainee_ByUsername(traineeUsername)).thenReturn(trainers);

        List<Trainer> result = trainerDAO.findAllTrainersNotAssignedToTrainee_ByUsername(traineeUsername);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainer.getId(), result.get(0).getId());
    }

    @Test
    public void testFindAllTrainerUsernames_ByPrefix() {
        String prefix = "test";
        List<String> usernames = Arrays.asList("testTrainer1", "testTrainer2");

        Mockito.when(trainerRepository.findAllUsernames_ByPrefix(prefix)).thenReturn(usernames);

        List<String> result = trainerDAO.findAllTrainerUsernames_ByPrefix(prefix);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testTrainer1", result.get(0));
    }

    @Test
    public void testUpdateTrainer() {
        Mockito.when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer result = trainerDAO.updateTrainer(trainer);

        assertNotNull(result);
        assertEquals(trainer.getId(), result.getId());
    }
}
