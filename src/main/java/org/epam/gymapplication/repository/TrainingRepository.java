package org.epam.gymapplication.repository;

import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t_t FROM TrainingType t_t")
    Set<TrainingType> getAllTrainingTypes();

    void deleteByTrainee_Id(Long id);
}
