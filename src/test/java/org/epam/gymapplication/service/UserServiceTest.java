package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    TraineeDAOImpl traineeDAO;

    @Mock
    TrainerDAOImpl trainerDAO;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUniqueUsername_WithNoSimilarUsernamesTest(){
        String firthName = "Firth";
        String secondName = "Last";
        String username = firthName + "." + secondName;

        Mockito.when(trainerDAO.findAllTrainerUsernames_ByPrefix(username))
                .thenReturn(new ArrayList<>());
        Mockito.when(traineeDAO.findAllTraineeUsernames_ByPrefix(username))
                .thenReturn(new ArrayList<>());

        String resultedUsername = userService.generateUniqueUsername(firthName, secondName);
        assertEquals(username, resultedUsername);
    }

    @Test
    void generateUniqueUsername_WithSimilarUsernamesTest(){
        String firthName = "Firth";
        String secondName = "Last";
        String username = firthName + "." + secondName;

        Mockito.when(trainerDAO.findAllTrainerUsernames_ByPrefix(username))
                .thenReturn(List.of("Firth.Last"));
        Mockito.when(traineeDAO.findAllTraineeUsernames_ByPrefix(username))
                .thenReturn(new ArrayList<>());

        String resultedUsername = userService.generateUniqueUsername(firthName, secondName);

        assertEquals(username + "1", resultedUsername);
    }

    @Test
    void generateRandomPasswordTest(){
        String firthPassword = Arrays.toString(userService.generateRandom_10CharsPassword());
        String secondPassword = Arrays.toString(userService.generateRandom_10CharsPassword());

        assertEquals(firthPassword.length(), secondPassword.length());
        assertNotEquals(firthPassword, secondPassword);
    }
}
