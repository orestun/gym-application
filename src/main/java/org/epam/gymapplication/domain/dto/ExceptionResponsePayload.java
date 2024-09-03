package org.epam.gymapplication.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponsePayload {
    String message;
    HttpStatus status;
    LocalDateTime timestamp;
    int statusCode;

    public ExceptionResponsePayload(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        timestamp = LocalDateTime.now();
        statusCode = status.value();
    }
}
