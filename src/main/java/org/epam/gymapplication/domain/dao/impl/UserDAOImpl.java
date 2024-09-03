package org.epam.gymapplication.domain.dao.impl;

import org.epam.gymapplication.domain.dao.UserDAO;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String username, String password) {
        userRepository.updatePasswordByUsername(password, username);
    }
}
