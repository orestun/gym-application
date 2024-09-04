package org.epam.gymapplication.service.impl;

import lombok.AllArgsConstructor;
import org.epam.gymapplication.domain.dao.UserDAO;
import org.epam.gymapplication.domain.dto.*;
import org.epam.gymapplication.exception.BadAuthenticationDataException;
import org.epam.gymapplication.exception.BlockedOperationException;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.service.IAuthService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {


    private final UserService userService;
    private final UserDAO userDAO;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public AuthRegistrationDTO registerTrainee(String firthName, String lastName, Date dateOfBirth, String address) {
        AuthDTO authGeneratedData = generateAuthData(firthName, lastName);
        TraineeBasicProfileDTO traineeDTO = TraineeBasicProfileDTO.builder().firstName(firthName).lastName(lastName).dateOfBirth(dateOfBirth).address(address).build();
        Trainee savedTrainee = traineeService.addTrainee(authGeneratedData, traineeDTO);

        String token = login(authGeneratedData).get("jwt").toString();
        return new AuthRegistrationDTO(savedTrainee.getUser().getUsername(), authGeneratedData.getPassword(), token);
    }

    public AuthRegistrationDTO registerTrainer(String firthName, String lastName, TrainingType specialization) {
        AuthDTO authGeneratedData = generateAuthData(firthName, lastName);
        TrainerBasicProfileDTO trainerDTO = TrainerBasicProfileDTO.builder().firstName(firthName).lastName(lastName).build();
        Trainer savedTrainer = trainerService.addTrainer(authGeneratedData, trainerDTO, specialization);

        String token = login(authGeneratedData).get("jwt").toString();
        return new AuthRegistrationDTO(savedTrainer.getUser().getUsername(), authGeneratedData.getPassword(), token);
    }

    public Map<String, Object> login(AuthDTO authRequest) {
        Authentication authentication = getAndTryAuthentication(authRequest);
        if (authentication.isAuthenticated()) {
            loginAttemptService.resetAttempts(authRequest.getUsername());
            String token = jwtTokenService.generateToken(authRequest.getUsername());
            return getAuthJwtTokenBody(token);
        }
        throw new BadAuthenticationDataException(ExceptionMessage.badAuthenticationData());
    }

    public Map<String, Object> changeLogin(ChangeLoginDTO changeLoginDTO) {
        AuthDTO authRequest = AuthDTO
                .builder()
                .username(changeLoginDTO.getUsername())
                .password(changeLoginDTO.getPassword()).build();
        Authentication authentication = getAndTryAuthentication(authRequest);
        if (authentication.isAuthenticated()) {
            loginAttemptService.resetAttempts(authRequest.getUsername());
            userDAO.changePassword(
                    changeLoginDTO.getUsername(),
                    passwordEncoder.encode(changeLoginDTO.getNewPassword()));
            String token = jwtTokenService.generateToken(authRequest.getUsername());
            return getAuthJwtTokenBody(token);
        } else {
            throw new BadAuthenticationDataException(ExceptionMessage.badAuthenticationData());
        }
    }

    private Authentication getAndTryAuthentication(AuthDTO authenticationRequest) {
        try {
            if (loginAttemptService.isBlocked(authenticationRequest.getUsername())) {
                throw new BlockedOperationException(ExceptionMessage.bruceForceProtection());
            }
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            loginAttemptService.registerAttempt(authenticationRequest.getUsername());
            throw new BadAuthenticationDataException(ExceptionMessage.badAuthenticationData());
        }
    }

    private Map<String, Object> getAuthJwtTokenBody(String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("jwt", token);
        return body;
    }

    private AuthDTO generateAuthData(String firthName, String lastName) {
        String username = userService.generateUniqueUsername(firthName, lastName);
        String password = String.copyValueOf(userService.generateRandom_10CharsPassword());

        return new AuthDTO(username, password);
    }
}
