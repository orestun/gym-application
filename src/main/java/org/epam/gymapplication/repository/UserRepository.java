package org.epam.gymapplication.repository;

import org.epam.gymapplication.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.username = ?2")
    int updatePasswordByUsername(String password, String username);
}
