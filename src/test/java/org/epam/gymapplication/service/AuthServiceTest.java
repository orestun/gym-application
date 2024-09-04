package org.epam.gymapplication.service;

import jakarta.servlet.http.HttpServletRequest;
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
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
                .isActive(true)
                .build();
        Trainee trainee = Trainee.builder()
                .user(user)
                .build();

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);

            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);

            when(userService.generateUniqueUsername("John", "Doe")).thenReturn("testuser");
            when(userService.generateRandom_10CharsPassword()).thenReturn("password123".toCharArray());
            when(traineeService.addTrainee(any(AuthDTO.class), any(TraineeBasicProfileDTO.class)))
                    .thenReturn(trainee);
            when(jwtTokenService.generateToken("testuser")).thenReturn("jwt-test-token");

            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            AuthRegistrationDTO registrationDTO = authService.registerTrainee("John", "Doe", new Date(), "123 Main St", mockRequest);

            assertEquals("jwt-test-token", registrationDTO.getJwtToken());

            verify(loginAttemptService).isBlocked(authDTO.getUsername(), "ip");
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userService).generateUniqueUsername("John", "Doe");
            verify(userService).generateRandom_10CharsPassword();
            verify(traineeService).addTrainee(any(AuthDTO.class), any(TraineeBasicProfileDTO.class));
            verify(jwtTokenService).generateToken("testuser");
        }
    }

    @Test
    void registerTrainerTest() {
        // Arrange
        AuthDTO authDTO = new AuthDTO("testuser", "password123");
        User user = User.builder()
                .firthName("John")
                .secondName("Doe")
                .username("testuser")
                .password("encodedpassword")
                .isActive(true)
                .build();
        Trainer trainer = Trainer.builder()
                .user(user)
                .specialization(new TrainingType())
                .build();

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);

            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);

            when(userService.generateUniqueUsername("John", "Doe")).thenReturn("testuser");
            when(userService.generateRandom_10CharsPassword()).thenReturn("password123".toCharArray());
            when(trainerService.addTrainer(any(AuthDTO.class), any(TrainerBasicProfileDTO.class), any(TrainingType.class)))
                    .thenReturn(trainer);
            when(jwtTokenService.generateToken("testuser")).thenReturn("jwt-test-token");

            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            AuthRegistrationDTO registrationDTO = authService.registerTrainer("John", "Doe", new TrainingType(), mockRequest);

            assertEquals("jwt-test-token", registrationDTO.getJwtToken());

            verify(loginAttemptService).isBlocked(authDTO.getUsername(), "ip");
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userService).generateUniqueUsername("John", "Doe");
            verify(userService).generateRandom_10CharsPassword();
            verify(trainerService).addTrainer(any(AuthDTO.class), any(TrainerBasicProfileDTO.class), any(TrainingType.class));
            verify(jwtTokenService).generateToken("testuser");
        }
    }

    @Test
    void loginWithBadCredentials() {
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);

            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            BadAuthenticationDataException exception = assertThrows(
                    BadAuthenticationDataException.class,
                    () -> authService.login(authDTO, mock(HttpServletRequest.class))
            );

            assertEquals(ExceptionMessage.badAuthenticationData(), exception.getMessage());
        }
    }

    @Test
    void loginWithBadData() {
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException(""));

            BadAuthenticationDataException exception = assertThrows(
                    BadAuthenticationDataException.class,
                    () -> authService.login(authDTO, mock(HttpServletRequest.class))
            );

            assertEquals(ExceptionMessage.badAuthenticationData(), exception.getMessage());
        }
    }

    @Test
    void changePasswordTest() {
        ChangeLoginDTO changeLoginDTO = ChangeLoginDTO.builder()
                .username("testuser")
                .password("password123")
                .newPassword("new-password")
                .build();
        AuthDTO authDTO = new AuthDTO("testuser", "password123");

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);

            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);

            when(passwordEncoder.encode(changeLoginDTO.getNewPassword())).thenReturn(changeLoginDTO.getNewPassword());
            when(jwtTokenService.generateToken("testuser")).thenReturn("jwt-test-token");

            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            String token = (String) authService.changeLogin(changeLoginDTO, mockRequest).get("jwt");

            assertEquals("jwt-test-token", token);

            verify(userDAO).changePassword(authDTO.getUsername(), changeLoginDTO.getNewPassword());
        }
    }

    @Test
    void changePassword_WithBadCredentialsTest() {
        ChangeLoginDTO changeLoginDTO = ChangeLoginDTO.builder()
                .username("testuser")
                .password("password123")
                .newPassword("new-password")
                .build();
        AuthDTO authDTO = new AuthDTO("testuser", "password123");

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(false);

            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            BadAuthenticationDataException exception = assertThrows(
                    BadAuthenticationDataException.class,
                    () -> authService.changeLogin(changeLoginDTO, mock(HttpServletRequest.class))
            );

            assertEquals(ExceptionMessage.badAuthenticationData(), exception.getMessage());
        }
    }

    @Test
    void tooManyAffordsToLoginTest() {
        AuthDTO authDTO = new AuthDTO("testuser", "bad-password123");

        try (MockedStatic<IpService> mockedStatic = mockStatic(IpService.class)) {
            mockedStatic.when(() -> IpService.getIpAddressFromHeader(any(HttpServletRequest.class)))
                    .thenReturn("ip");

            when(loginAttemptService.isBlocked(authDTO.getUsername(), "ip")).thenReturn(true);

            BlockedOperationException exception = assertThrows(
                    BlockedOperationException.class,
                    () -> authService.login(authDTO, mock(HttpServletRequest.class))
            );

            assertEquals(ExceptionMessage.bruceForceProtection(), exception.getMessage());
        }
    }
}
