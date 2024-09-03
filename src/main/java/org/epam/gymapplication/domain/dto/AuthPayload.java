package org.epam.gymapplication.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthPayload {
    private String username;
    private String password;
}
