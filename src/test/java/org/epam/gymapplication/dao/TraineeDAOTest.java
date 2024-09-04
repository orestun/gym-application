package org.epam.gymapplication.dao;

import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.repository.TraineeRepository;
import org.epam.gymapplication.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(User.builder().username("testUser").password("password").build());
    }

    @Test
    public void testGetTraineeByUsername_Success() {
        String username = "testUser";

        Mockito.when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        Trainee result = traineeDAO.getTraineeByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUser().getUsername());
    }

    @Test()
    public void testGetTraineeByUsername_TraineeNotFound() {
        String username = "nonExistingUser";

        Mockito.when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        assertThrows(ItemNotExistsException.class, () -> traineeDAO.getTraineeByUsername(username));
    }

    @Test
    public void testAddTrainee() {
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeDAO.addTrainee(trainee);

        assertNotNull(result);
        assertEquals(trainee.getId(), result.getId());
    }

    @Test
    public void testUpdateTrainee() {
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeDAO.updateTrainee(trainee);

        assertNotNull(result);
        assertEquals(trainee.getId(), result.getId());
    }

    @Test
    public void testDeleteTraineeProfileByUsername_Success() {
        String username = "testUser";

        Mockito.when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        traineeDAO.deleteTraineeProfileByUsername(username);

        Mockito.verify(traineeRepository).clearTrainersAssociationsFromTraineeByUsername(trainee.getId());
        Mockito.verify(trainingRepository).deleteByTrainee_Id(trainee.getId());
        Mockito.verify(traineeRepository).deleteByUserUsername(username);
    }

    @Test
    public void testDeleteTraineeProfileByUsername_TraineeNotFound() {
        String username = "nonExistingUser";

        Mockito.when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        assertThrows(ItemNotExistsException.class, () -> traineeDAO.deleteTraineeProfileByUsername(username));
    }

    @Test
    public void testUpdateActiveStatus() {
        String username = "testUser";
        boolean active = true;

        traineeDAO.updateActiveStatus(username, active);

        Mockito.verify(traineeRepository).updateActiveStatusByUsername(username, active);
    }

    @Test
    public void testFindAllTraineeUsernames_ByPrefix() {
        String prefix = "test";
        List<String> usernames = Arrays.asList("testUser1", "testUser2");

        Mockito.when(traineeRepository.findAllUsernames_ByPrefix(prefix)).thenReturn(usernames);

        List<String> result = traineeDAO.findAllTraineeUsernames_ByPrefix(prefix);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testUser1", result.get(0));
    }

    @Test
    public void existTraineeByUsername(){
        String username1 = "normal-username";
        String username2 = "bad-username";

        Mockito.when(traineeRepository.existsByUser_Username(username1)).thenReturn(true);
        Mockito.when(traineeRepository.existsByUser_Username(username2)).thenReturn(false);

        assertTrue(traineeDAO.existsTraineeByUsername(username1));
        assertFalse(traineeDAO.existsTraineeByUsername(username2));
    }
}
