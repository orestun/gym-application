package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.domain.dao.UserDAO;
import org.epam.gymapplication.domain.dao.impl.UserDAOImpl;
import org.epam.gymapplication.exception.ItemNotExistsException;
import org.epam.gymapplication.domain.model.User;
import org.epam.gymapplication.domain.model.UserDetailsImpl;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;

    public UserDetailsServiceImpl(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.getUserByUsername(username);
        return user.map(UserDetailsImpl::new).orElseThrow(() -> new ItemNotExistsException(ExceptionMessage.userNotFoundByUsername(username)));
    }
}
