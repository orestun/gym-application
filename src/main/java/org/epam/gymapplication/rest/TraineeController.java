package org.epam.gymapplication.rest;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.epam.gymapplication.domain.dto.TraineeBasicProfileDTO;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.service.impl.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("trainee")
@AllArgsConstructor
@Validated
public class TraineeController {

    private final TraineeService traineeService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @Operation(summary = "Get trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("{username}")
    public ResponseEntity<?> getTraineeProfileByUsername(@PathVariable String username){
        logger.info("Received request to get trainee by username: {}", username);
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return ResponseEntity.ok(generateResponseBodyForTrainee(trainee));
    }

    @Operation(summary = "Update trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping("update")
    public ResponseEntity<?> updateTraineeProfile(@RequestBody @Valid TraineeBasicProfileDTO traineeDTO){
        logger.info("Received request to update trainee with username: {}", traineeDTO.getUsername());
        Trainee trainee = traineeService.updateTrainee(traineeDTO);
        return ResponseEntity.ok(generateResponseBodyForTrainee(trainee));
    }

    @Operation(summary = "Delete trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @DeleteMapping("delete/{username}")
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable String username){
        logger.info("Received request to delete trainee by username: {}", username);
        traineeService.deleteTraineeProfileByUsername(username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "De-/Activate trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee de-/activated"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PatchMapping("update/active-status")
    public ResponseEntity<String> updateActiveStatusByUsername(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "active") boolean isActive){
        logger.info("Received request to update status by username: {}", username);
        traineeService.updateActiveStatusByUsername(username, isActive);
        return ResponseEntity.ok(isActive ? "activated" : "deactivated");
    }

    @Operation(summary = "Update list of trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer list updated"),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping("{username}/update/trainer-list")
    public ResponseEntity<List<Map<String, Object>>> updateTrainerListByUsername(
            @PathVariable(value = "username") String username,
            @RequestBody List<String> trainerList){
        logger.info("Received request to update trainer list by username: {}", username);
        List<Trainer> updatedTrainerList = traineeService.updateTrainerListForTrainee_ByUsername(username, trainerList);
        return ResponseEntity.ok(updatedTrainerList
                        .stream()
                        .map(this::generateResponseBodyForTrainer)
                        .toList());
    }

    private Map<String, Object> generateResponseBodyForTrainee(Trainee trainee){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("username", trainee.getUser().getUsername());
        responseBody.put("firthName", trainee.getUser().getFirthName());
        responseBody.put("secondName", trainee.getUser().getSecondName());
        if(trainee.getDateOfBirth() != null){
            responseBody.put("dateOfBirth", trainee.getDateOfBirth());
        }
        if(trainee.getAddress() != null){
            responseBody.put("address", trainee.getAddress());
        }
        responseBody.put("isActive", trainee.getUser().isActive());
        if(trainee.getTrainers() != null){
            responseBody.put("trainers", trainee.getTrainers()
                    .stream()
                    .map(this::generateResponseBodyForTrainer));
        }
        return responseBody;
    }

    private Map<String, Object> generateResponseBodyForTrainer(Trainer trainer){
        Map<String, Object> trainerResponseBody = new HashMap<>();
        trainerResponseBody.put("username", trainer.getUser().getUsername());
        trainerResponseBody.put("firthName", trainer.getUser().getFirthName());
        trainerResponseBody.put("secondName", trainer.getUser().getSecondName());
        trainerResponseBody.put("specialization", trainer.getSpecialization());
        return trainerResponseBody;
    }
}
