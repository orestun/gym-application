package org.epam.gymapplication.repository;

import org.epam.gymapplication.domain.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUser_Username(String username);

    @Transactional
    void deleteByUserUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM trainer_trainee WHERE trainee_id=:id", nativeQuery = true)
    void clearTrainersAssociationsFromTraineeByUsername(@Param(value = "id") Long id);

    @Transactional
    @Modifying
    @Query("update Trainee t set t.user.isActive = ?2 where t.user.username = ?1")
    void updateActiveStatusByUsername(String username, boolean isActive);

    @Query("select t.user.username from Trainee t where t.user.username like CONCAT(:username,'%')")
    List<String> findAllUsernames_ByPrefix(@Param("username") String username);
}
