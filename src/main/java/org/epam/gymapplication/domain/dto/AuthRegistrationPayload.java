package org.epam.gymapplication.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AuthRegistrationPayload {
    private String username;
    private String password;
    private String jwtToken;
}
