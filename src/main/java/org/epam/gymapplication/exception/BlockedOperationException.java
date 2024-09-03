package org.epam.gymapplication.exception;

public class BlockedOperationException extends RuntimeException {
    public BlockedOperationException(String message) {
        super(message);
    }
}
