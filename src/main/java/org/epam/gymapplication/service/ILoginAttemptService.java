package org.epam.gymapplication.service;

public interface ILoginAttemptService {
    boolean isBlocked(String username);
    void registerAttempt(String username);
    void resetAttempts(String username);
}
