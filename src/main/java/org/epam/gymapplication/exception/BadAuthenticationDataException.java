package org.epam.gymapplication.exception;

public class BadAuthenticationDataException extends RuntimeException{

    public BadAuthenticationDataException(String message) {
        super(message);
    }
}
