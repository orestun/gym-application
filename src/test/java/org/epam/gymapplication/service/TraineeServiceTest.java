package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.TraineeBasicProfileDTO;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.service.impl.TraineeService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TraineeServiceTest {

    @Mock
    private TraineeDAOImpl traineeDAO;

    @Mock
    private TrainerDAOImpl trainerDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "testuser";
        Trainee expectedTrainee = new Trainee();
        when(traineeDAO.getTraineeByUsername(username)).thenReturn(expectedTrainee);

        Trainee result = traineeService.getTraineeByUsername(username);

        assertEquals(expectedTrainee, result);
        verify(traineeDAO, times(1)).getTraineeByUsername(username);
    }

    @Test
    public void testGetTraineeByUsername_TraineeNotFound() {
        String username = "testuser";
        when(traineeDAO.getTraineeByUsername(username))
                .thenThrow(new ItemNotExistsException(ExceptionMessage.traineeNotFoundByUsername(username)));

        ItemNotExistsException e = assertThrows(ItemNotExistsException.class, () -> traineeService.getTraineeByUsername(username));
        assertEquals(ExceptionMessage.traineeNotFoundByUsername(username), e.getMessage());
    }

    @Test
    public void testAddTrainee() {
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        TraineeBasicProfileDTO traineeDTO = TraineeBasicProfileDTO.builder().firstName("John").lastName("Doe").address("123 Main St").build();
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainee trainee = Trainee.builder().user(user).build();
        when(passwordEncoder.encode(authDTO.getPassword())).thenReturn("encodedpassword");
        when(traineeDAO.addTrainee(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.addTrainee(authDTO, traineeDTO);

        assertEquals(trainee, result);
        verify(passwordEncoder, times(1)).encode(authDTO.getPassword());
        verify(traineeDAO, times(1)).addTrainee(any(Trainee.class));
    }

    @Test
    public void testAddTrainee_withNullAddressAndNullDate() {
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        TraineeBasicProfileDTO traineeDTO = TraineeBasicProfileDTO.builder().firstName("John").lastName("Doe").address("123 Main St").dateOfBirth(new Date()).build();
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainee trainee = Trainee.builder().user(user).build();
        when(passwordEncoder.encode(authDTO.getPassword())).thenReturn("encodedpassword");
        when(traineeDAO.addTrainee(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.addTrainee(authDTO, traineeDTO);

        assertEquals(trainee, result);
        verify(passwordEncoder, times(1)).encode(authDTO.getPassword());
        verify(traineeDAO, times(1)).addTrainee(any(Trainee.class));
    }

    @Test
    public void testUpdateTrainee() {
        TraineeBasicProfileDTO traineeDTO = TraineeBasicProfileDTO.builder().username("testuser").firstName("John").dateOfBirth(new Date(2344324)).lastName("Doe").isActive(true).address("123 Main St").build();
        User user = User.builder()
                .firthName("OldName")
                .secondName("OldSurname")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainee trainee = Trainee.builder().user(user).build();
        when(traineeDAO.getTraineeByUsername("testuser")).thenReturn(trainee);
        when(traineeDAO.updateTrainee(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(traineeDTO);

        assertEquals("John", trainee.getUser().getFirthName());
        assertEquals("Doe", trainee.getUser().getSecondName());
        assertTrue(trainee.getUser().isActive());
        assertEquals("123 Main St", trainee.getAddress());
        verify(traineeDAO, times(1)).getTraineeByUsername("testuser");
        verify(traineeDAO, times(1)).updateTrainee(any(Trainee.class));
    }

    @Test
    public void testUpdateTrainee_WithNullAddressAndNullDate() {
        TraineeBasicProfileDTO traineeDTO = TraineeBasicProfileDTO.builder().username("testuser").firstName("John").lastName("Doe").isActive(true).build();
        User user = User.builder()
                .firthName("OldName")
                .secondName("OldSurname")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainee trainee = Trainee.builder().user(user).build();
        when(traineeDAO.getTraineeByUsername("testuser")).thenReturn(trainee);
        when(traineeDAO.updateTrainee(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(traineeDTO);

        assertNull(result.getDateOfBirth());
        assertNull(result.getAddress());
    }

    @Test
    void testDeleteTraineeProfileByUsername() {
        String username = "testUser";
        traineeService.deleteTraineeProfileByUsername(username);
        Mockito.verify(traineeDAO, Mockito.times(1)).deleteTraineeProfileByUsername(username);
    }

    @Test
    void testUpdateActiveStatusByUsername() {
        String username = "testUser";
        boolean isActive = true;
        traineeService.updateActiveStatusByUsername(username, isActive);
        Mockito.verify(traineeDAO, Mockito.times(1)).updateActiveStatus(username, isActive);
    }

    @Test
    void testUpdateTrainerListForTrainee_ByUsername() {
        String traineeUsername = "traineeUser";
        List<String> trainerUsernameList = List.of("trainer1");
        Set<Trainer> mockTrainerSet = Set.of(
                Trainer.builder()
                        .user(User.builder().username("trainer1").build())
                        .build());
        Mockito.when(traineeDAO.getTraineeByUsername(traineeUsername)).thenReturn(Trainee.builder().trainers(mockTrainerSet).build());
        Mockito.when(traineeDAO.updateTrainee(any(Trainee.class))).thenReturn(Trainee.builder().trainers(mockTrainerSet).build());
        Mockito.when(trainerDAO.getTrainerByUsername("trainer1"))
                .thenReturn(Optional.of(Trainer.builder()
                                .user(User.builder().username("trainer1").build())
                                .trainee(new HashSet<>())
                                .build()));
        List<Trainer> result = traineeService.updateTrainerListForTrainee_ByUsername(traineeUsername, trainerUsernameList);

        assertEquals(1, result.size());
        assertEquals(result.get(0).getUser().getUsername(), trainerUsernameList.get(0));
    }
}