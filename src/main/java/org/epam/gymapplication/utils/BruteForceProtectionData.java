package org.epam.gymapplication.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BruteForceProtectionData {
    private int attempt;
    private long lastAttemptTimeMillis;
    private String ipUserToken;

    public BruteForceProtectionData(String ipUserToken) {
        this.attempt = 0;
        this.lastAttemptTimeMillis = System.currentTimeMillis();
        this.ipUserToken = ipUserToken;
    }
}
