package org.epam.gymapplication.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthDTO {
    String username;
    String password;
}
