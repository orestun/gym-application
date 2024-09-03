package org.epam.gymapplication.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class ChangeLoginDTO {
    String username;
    String password;
    String newPassword;
}
