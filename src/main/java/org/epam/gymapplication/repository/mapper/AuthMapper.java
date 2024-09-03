package org.epam.gymapplication.repository.mapper;

import org.epam.gymapplication.domain.dto.*;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthDTO mapAuthPayloadToAuthDTO(AuthPayload payload){
        return AuthDTO.builder()
                .username(payload.getUsername())
                .password(payload.getPassword()).build();
    }

    public ChangeLoginDTO mapChangeLoginPayloadToChangeLoginDTO(ChangeLoginPayload payload){
        return ChangeLoginDTO.builder()
                .username(payload.getUsername())
                .password(payload.getPassword())
                .newPassword(payload.getNewPassword()).build();
    }

    public AuthRegistrationPayload mapAuthRegistrationDTOToAuthRegistrationPayload(AuthRegistrationDTO registrationDTO){
        return AuthRegistrationPayload.builder()
                .username(registrationDTO.getUsername())
                .password(registrationDTO.getPassword())
                .jwtToken(registrationDTO.getJwtToken()).build();
    }
}
