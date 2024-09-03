package org.epam.gymapplication.dao;

import org.epam.gymapplication.domain.dao.impl.UserDAOImpl;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDAOImpl userDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByUsername_UserExists() {
        String username = "testuser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userDAO.getUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserByUsername_UserDoesNotExist() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userDAO.getUserByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    void testChangePassword() {
        String username = "testuser";
        String newPassword = "newpassword";

        userDAO.changePassword(username, newPassword);
        verify(userRepository, times(1)).updatePasswordByUsername(newPassword, username);
    }
}
