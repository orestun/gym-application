package org.epam.gymapplication.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.epam.gymapplication.domain.dto.*;
import org.epam.gymapplication.repository.mapper.AuthMapper;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.service.impl.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("auth")
@Validated
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, AuthMapper authMapper) {
        this.authService = authService;
        this.authMapper = authMapper;
    }

    @Operation(summary = "Register new trainee user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee successfully added"),
            @ApiResponse(responseCode = "404", description = "Issue with finding trainee with similar username"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping("register/trainee")
    public AuthRegistrationPayload registerTrainee(
            @RequestParam(value = "firthName") String firthName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "dateOfBirth", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(value = "address", required = false) String address,
            HttpServletRequest request) {

        logger.info("Received request to register trainee");
        AuthRegistrationDTO registrationDTO = authService.registerTrainee(firthName, lastName, dateOfBirth, address, request);
        return authMapper.mapAuthRegistrationDTOToAuthRegistrationPayload(registrationDTO);
    }

    @Operation(summary = "Register new trainer user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer successfully added"),
            @ApiResponse(responseCode = "404", description = "Issue with finding trainer with similar username"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping("register/trainer")
    public AuthRegistrationPayload registerTrainer(@RequestParam("firthName") String firthName,
                                                   @RequestParam("lastName") String lastName,
                                                   @RequestParam("id") Long trainingTypeId,
                                                   HttpServletRequest request) {
        logger.info("Received request to register trainer");
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeId);
        AuthRegistrationDTO registrationDTO = authService.registerTrainer(firthName, lastName, trainingType, request);
        return authMapper.mapAuthRegistrationDTOToAuthRegistrationPayload(registrationDTO);
    }

    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Bad input credentials"),
            @ApiResponse(responseCode = "404", description = "Bad input username"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthPayload authPayload, HttpServletRequest request) {
        logger.info("Received request to login '{}'", authPayload.getUsername());
        AuthDTO authDTO = authMapper.mapAuthPayloadToAuthDTO(authPayload);
        return ResponseEntity.ok(authService.login(authDTO, request));
    }

    @Operation(summary = "Change login to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Bad input credentials"),
            @ApiResponse(responseCode = "404", description = "Bad input username"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping("change-login")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody ChangeLoginPayload authPayload, HttpServletRequest request) {
        logger.info("Received request to change login '{}'", authPayload.getUsername());
        ChangeLoginDTO changeLoginDTO = authMapper.mapChangeLoginPayloadToChangeLoginDTO(authPayload);
        return ResponseEntity.ok(authService.changeLogin(changeLoginDTO, request));
    }

    @Operation(summary = "Log out user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged out")
    })
    @GetMapping("logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("You have been logged out successfully");
    }

}
