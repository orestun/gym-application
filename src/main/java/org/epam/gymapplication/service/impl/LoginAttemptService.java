package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.utils.BruceForceProtectionData;
import org.epam.gymapplication.utils.SecurityConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final Map<String, BruceForceProtectionData> attemptCache = new HashMap<>();

    public BruceForceProtectionData processUser(String username) {
        return getAttempts(username);
    }

    @Cacheable(value = "loginAttempts", key = "#username", cacheManager = "cacheManager")
    public BruceForceProtectionData getAttempts(String username) {
        return attemptCache.getOrDefault(username, new BruceForceProtectionData(username));
    }

    @CachePut(value = "loginAttempts", key = "#username")
    public BruceForceProtectionData updateAttempts(String username, BruceForceProtectionData data) {
        attemptCache.put(username, data);
        return data;
    }

    @CacheEvict(value = "loginAttempts", key = "#username")
    public void clearAttempts(String username) {
        attemptCache.remove(username);
    }

    public boolean isBlocked(String username) {
        BruceForceProtectionData data =  processUser(username);
        if (data.getAttempt() >= SecurityConstants.MAX_LOGIN_ATTEMPTS_3_FOR_SPECIFIC_TIME){
            if(System.currentTimeMillis() - data.getLastAttemptTimeMillis() < SecurityConstants.LOCK_TIME_DURATION_5_MIN_IN_MILLIS){
                return true;
            }
            clearAttempts(username);
        }
        return false;
    }

    public void registerAttempt(String username) {
        BruceForceProtectionData data = processUser(username);
        data.setAttempt(data.getAttempt() + 1);
        data.setLastAttemptTimeMillis(System.currentTimeMillis());
        updateAttempts(username, data);
    }

    public void resetAttempts(String username) {
        clearAttempts(username);
    }

}
