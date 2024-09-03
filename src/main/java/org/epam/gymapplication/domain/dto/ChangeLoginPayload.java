package org.epam.gymapplication.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class ChangeLoginPayload {
    String username;
    String password;
    String newPassword;
}
