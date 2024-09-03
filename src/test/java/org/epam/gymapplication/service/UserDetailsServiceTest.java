package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dao.impl.UserDAOImpl;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsServiceTest {

    @Mock
    UserDAOImpl userDAO;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameSuccessTest(){
        String username = "test";
        Mockito.when(userDAO.getUserByUsername(username))
                .thenReturn(Optional.of(User.builder().username(username).password("pass").build()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertEquals(userDetails.getUsername(), username);
        assertEquals(userDetails.getPassword(), "pass");
    }

    @Test
    void loadUserByUsernameFailTest(){
        String username = "test";
        Mockito.when(userDAO.getUserByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotExistsException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}
