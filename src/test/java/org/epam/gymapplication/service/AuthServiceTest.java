package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dao.impl.UserDAOImpl;
import org.epam.gymapplication.domain.dto.*;
import org.epam.gymapplication.exception.BadAuthenticationDataException;
import org.epam.gymapplication.exception.BlockedOperationException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.service.impl.*;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class AuthServiceTest {

    @Mock
    UserService userService;
    @Mock
    UserDAOImpl userDAO;
    @Mock
    JwtTokenService jwtTokenService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TrainerService trainerService;
    @Mock
    TraineeService traineeService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    LoginAttemptService loginAttemptService;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTraineeTest() {
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainee trainee = Trainee.builder().user(user).build();

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        Mockito.when(userService.generateUniqueUsername("John", "Doe")).thenReturn("testuser");
        Mockito.when(userService.generateRandom_10CharsPassword()).thenReturn("password123".toCharArray());
        Mockito.when(traineeService.addTrainee(any(AuthDTO.class),any(TraineeBasicProfileDTO.class)))
                .thenReturn(trainee);
        Mockito.when(jwtTokenService.generateToken("testuser"))
                .thenReturn("jwt-test-token");

        AuthRegistrationDTO registrationDTO = authService.registerTrainee("John", "Doe", new Date(), "123 Main St");
        assertEquals(registrationDTO.getJwtToken(), "jwt-test-token");
    }

    @Test
    void registerTrainerTest() {
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true).build();
        Trainer trainer = Trainer.builder().user(user).specialization(new TrainingType()).build();

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        Mockito.when(userService.generateUniqueUsername("John", "Doe")).thenReturn("testuser");
        Mockito.when(userService.generateRandom_10CharsPassword()).thenReturn("password123".toCharArray());
        Mockito.when(trainerService.addTrainer(any(AuthDTO.class),any(TrainerBasicProfileDTO.class), any(TrainingType.class)))
                .thenReturn(trainer);
        Mockito.when(jwtTokenService.generateToken("testuser"))
                .thenReturn("jwt-test-token");

        AuthRegistrationDTO registrationDTO = authService.registerTrainer("John", "Doe",  new TrainingType());
        assertEquals(registrationDTO.getJwtToken(), "jwt-test-token");
    }

    @Test
    void loginWithBadCredentials(){
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(false);

        BadAuthenticationDataException exception = assertThrows(
                BadAuthenticationDataException.class,
                () -> authService.login(authDTO));

        assertEquals(exception.getMessage(), ExceptionMessage.badAuthenticationData());
    }

    @Test
    void loginWithBadData(){
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException(""));

        BadAuthenticationDataException exception = assertThrows(
                BadAuthenticationDataException.class,
                () -> authService.login(authDTO));

        assertEquals(exception.getMessage(), ExceptionMessage.badAuthenticationData());
    }

    @Test
    void changePasswordTest(){
        ChangeLoginDTO changeLoginDTO = ChangeLoginDTO.builder().username("testuser").password("password123").newPassword("new-password").build();
        AuthDTO authDTO = new AuthDTO("testuser", "password123");

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        Mockito.when(passwordEncoder.encode(changeLoginDTO.getNewPassword())).thenReturn(changeLoginDTO.getNewPassword());
        Mockito.when(jwtTokenService.generateToken("testuser"))
                .thenReturn("jwt-test-token");

        String token = (String) authService.changeLogin(changeLoginDTO).get("jwt");

        Mockito.verify(userDAO, times(1)).changePassword(authDTO.getUsername(), changeLoginDTO.getNewPassword());
        assertEquals(token, "jwt-test-token");
    }

    @Test
    void changePassword_WithBadCredentialsTest(){
        ChangeLoginDTO changeLoginDTO = ChangeLoginDTO.builder().username("testuser").password("password123").newPassword("new-password").build();
        AuthDTO authDTO = new AuthDTO("testuser", "password123");

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(false);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(false);

        BadAuthenticationDataException exception = assertThrows(
                BadAuthenticationDataException.class,
                () -> authService.changeLogin(changeLoginDTO));

        assertEquals(exception.getMessage(), ExceptionMessage.badAuthenticationData());
    }

    @Test
    void tooManyAffordsToLoginTest(){
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        Mockito.when(loginAttemptService.isBlocked(authDTO.getUsername()))
                .thenReturn(true);

        BlockedOperationException exception = assertThrows(
                BlockedOperationException.class,
                () -> authService.login(authDTO));

        assertEquals(exception.getMessage(), ExceptionMessage.bruceForceProtection());
    }
}
