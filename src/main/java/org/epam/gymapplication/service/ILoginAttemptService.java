package org.epam.gymapplication.service;

public interface ILoginAttemptService {
    boolean isBlocked(String username, String ip);
    void registerAttempt(String username, String ip);
    void resetAttempts(String username, String ip);
}
