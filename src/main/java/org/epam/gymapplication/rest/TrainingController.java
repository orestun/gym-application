package org.epam.gymapplication.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainingDTO;
import org.epam.gymapplication.domain.dto.TrainingPayload;
import org.epam.gymapplication.repository.mapper.TrainingMapper;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.service.impl.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("training")
@Validated
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;
    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    public TrainingController(TrainingService trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    @Operation(summary = "Add training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training added"),
            @ApiResponse(responseCode = "404", description = "Trainer or trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping
    public ResponseEntity<?> addTraining(@RequestBody @Valid TrainingPayload trainingPayload) {
        logger.info("Received request to add training");
        TrainingDTO trainingDTO = trainingMapper.mapTrainingDTOFromTrainingPayload(trainingPayload);
        trainingService.addTraining(trainingDTO);
        return ResponseEntity.ok("Training added successfully");
    }

    @Operation(summary = "Get training list for trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found"),
            @ApiResponse(responseCode = "404", description = "Trainee with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("trainee/{username}")
    public List<TrainingPayload> getTraineeTrainingList(
            @PathVariable(value = "username") String username,
            @RequestParam(value = "periodFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(value = "periodTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) {
        logger.info("Received request to get training list for trainee '{}'", username);
        TraineeTrainingCriteriaDTO criteriaDTO = TraineeTrainingCriteriaDTO.builder().username(username).periodFrom(periodFrom).periodTo(periodTo).trainerName(trainerName).trainingType(trainingType).build();
        return trainingService.getTraineeTrainingList(criteriaDTO);
    }

    @Operation(summary = "Get all training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training types found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("training-types")
    public Set<TrainingType> getAllTrainingTypes() {
        logger.info("Received request to get all training types");
        return trainingService.getAllTrainingTypes();
    }

    @Operation(summary = "Get training list for trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found"),
            @ApiResponse(responseCode = "404", description = "Trainer with this username doesn`t exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("trainer/{username}")
    public List<TrainingPayload> getTrainerTrainingList(
            @PathVariable(value = "username") String username,
            @RequestParam(value = "periodFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(value = "periodTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(value = "traineeName", required = false) String trainerName){
        logger.info("Received request to get training list for trainer '{}'", username);
        TrainerTrainingCriteriaDTO criteriaDTO = TrainerTrainingCriteriaDTO.builder().username(username).periodFrom(periodFrom).periodTo(periodTo).traineeName(trainerName).build();
        return trainingService.getTrainerTrainingList(criteriaDTO);
    }
}
