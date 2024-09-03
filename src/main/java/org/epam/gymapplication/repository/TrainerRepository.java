package org.epam.gymapplication.repository;

import org.epam.gymapplication.domain.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUser_Username(String username);

    @Query(value =
            "SELECT DISTINCT t.id AS id, trainer_u.id AS user_id, trainer_u.username, trainer_u.firth_name, trainer_u.second_name, trainer_u.is_active, t.specialization_id " +
            "FROM trainer t " +
            "JOIN user trainer_u ON t.user_id = trainer_u.id " +
            "LEFT JOIN training_type ts on t.id = t.specialization_id " +
            "LEFT JOIN trainer_trainee t_t ON t.id = t_t.trainer_id " +
            "LEFT JOIN trainee tr ON t_t.trainee_id = tr.id " +
            "LEFT JOIN user trainee_u ON tr.user_id = trainee_u.id " +
                    "WHERE trainer_u.is_active = true AND trainee_u.username = :username", nativeQuery = true)
    List<Trainer> findAllTrainersNotAssignedToTrainee_ByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("update Trainer t set t.user.isActive = ?2 where t.user.username = ?1")
    void updateActiveStatusByUsername(String username, boolean isActive);

    @Query("select t.user.username from Trainer t where t.user.username like CONCAT(:username,'%')")
    List<String> findAllUsernames_ByPrefix(@Param("username") String username);
}
