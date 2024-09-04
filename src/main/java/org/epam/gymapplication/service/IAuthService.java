package org.epam.gymapplication.service;

import org.epam.gymapplication.domain.dto.AuthDTO;
import org.epam.gymapplication.domain.dto.AuthRegistrationDTO;
import org.epam.gymapplication.domain.dto.ChangeLoginDTO;
import org.epam.gymapplication.domain.model.TrainingType;

import java.util.Date;
import java.util.Map;

public interface IAuthService {
    AuthRegistrationDTO registerTrainee(String firthName, String lastName, Date dateOfBirth, String address);
    AuthRegistrationDTO registerTrainer(String firthName, String lastName, TrainingType specialization);
    Map<String, Object> login(AuthDTO authRequest);
    Map<String, Object> changeLogin(ChangeLoginDTO changeLoginDTO);
}
