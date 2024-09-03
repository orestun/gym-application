package org.epam.gymapplication.domain.dao;

import org.epam.gymapplication.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO {
    Optional<User> getUserByUsername(String username);
    void changePassword(String username, String password);
}
