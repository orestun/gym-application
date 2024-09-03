package org.epam.gymapplication.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BruceForceProtectionData {
    private int attempt;
    private long lastAttemptTimeMillis;
    private String username;

    public BruceForceProtectionData(String username) {
        this.attempt = 0;
        this.lastAttemptTimeMillis = System.currentTimeMillis();
        this.username = username;
    }
}
