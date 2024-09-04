package org.epam.gymapplication.service;

import jakarta.servlet.http.HttpServletRequest;
import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.AuthRegistrationDTO;
import org.epam.gymapplication.domain.dto.ChangeLoginDTO;
import org.epam.gymapplication.domain.model.TrainingType;

import java.util.Date;
import java.util.Map;

public interface IAuthService {
    AuthRegistrationDTO registerTrainee(String firthName, String lastName, Date dateOfBirth, String address, HttpServletRequest request);
    AuthRegistrationDTO registerTrainer(String firthName, String lastName, TrainingType specialization, HttpServletRequest request);
    Map<String, Object> login(AuthDTO authRequest, HttpServletRequest request);
    Map<String, Object> changeLogin(ChangeLoginDTO changeLoginDTO, HttpServletRequest request);
}
