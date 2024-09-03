package org.epam.gymapplication.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.epam.gymapplication.domain.dto.TrainerBasicProfileDTO;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.service.impl.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("trainer")
@Validated
public class TrainerController {
    private final TrainerService trainerService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(summary = "Get trainer by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found"),
            @ApiResponse(responseCode = "404", description = "Trainer with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("{username}")
    public ResponseEntity<?> getTrainerByUsername(@PathVariable String username){
        logger.info("Received request to get trainer by username: {}", username);
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return ResponseEntity.ok(generateResponseBodyForTrainer(trainer));
    }

    @Operation(summary = "Update trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated"),
            @ApiResponse(responseCode = "404", description = "Trainer with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping("update")
    public ResponseEntity<?> updateTrainer(@RequestBody @Valid TrainerBasicProfileDTO trainerDTO){
        logger.info("Received request to update trainer with username: {}", trainerDTO.getUsername());
        Trainer trainer = trainerService.updateTrainer(trainerDTO);
        return ResponseEntity.ok(generateResponseBodyForTrainer(trainer));
    }

    @Operation(summary = "De-/Activate trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer de-/activated"),
            @ApiResponse(responseCode = "404", description = "Trainer with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PatchMapping("update/active-status")
    public ResponseEntity<String> updateActiveStatusByUsername(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "active") boolean isActive){
        logger.info("Received request to update status by username: {}", username);
        trainerService.updateActiveStatusByUsername(username, isActive);
        return ResponseEntity.ok(isActive ? "activated" : "deactivated");
    }

    @Operation(summary = "Get all ACTIVE trainers that are not assigned to specific trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers are found"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("not-assigned-to-trainee/{username}")
    public ResponseEntity<?> getAllActiveTrainersNotAssignedToTrainee_ByUsername(@PathVariable String username){
        logger.info("Received request to get trainer all NOT ASSIGNED TO TRAINEE trainers by username: {}", username);
        List<Trainer> trainers = trainerService.findAllTrainersNotAssignedToTrainee_ByUsername(username);
        return ResponseEntity.ok(trainers
                        .stream()
                        .map(this::generateResponseBodyForBasicTrainerProfile));
    }

    private Map<String, Object> generateResponseBodyForTrainer(Trainer trainer){
        Map<String, Object> responseBody = generateResponseBodyForBasicTrainerProfile(trainer);
        responseBody.put("isActive", trainer.getUser().isActive());
        return responseBody;

    }

    private Map<String, Object> generateResponseBodyForTrainees(Trainee trainee){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("username", trainee.getUser().getUsername());
        responseBody.put("firthName", trainee.getUser().getFirthName());
        responseBody.put("lastName", trainee.getUser().getSecondName());
        return responseBody;
    }

    private Map<String, Object> generateResponseBodyForBasicTrainerProfile(Trainer trainer){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("username", trainer.getUser().getUsername());
        responseBody.put("firthName", trainer.getUser().getFirthName());
        responseBody.put("lastName", trainer.getUser().getSecondName());
        if(trainer.getSpecialization() != null){
            responseBody.put("specialization", generateResponseBodyForSpecialization(trainer.getSpecialization()));
        }
        return responseBody;
    }

    private Map<String, Object> generateResponseBodyForSpecialization(TrainingType specialization){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", specialization.getId());
        responseBody.put("trainingTypeName", specialization.getTrainingTypeName());
        return responseBody;
    }
}
