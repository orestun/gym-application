package org.epam.gymapplication.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthRegistrationDTO {
    private String username;
    private String password;
    private String jwtToken;
}
