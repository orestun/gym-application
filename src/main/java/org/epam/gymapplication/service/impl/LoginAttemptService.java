package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.service.ILoginAttemptService;
import org.epam.gymapplication.utils.BruteForceProtectionData;
import org.epam.gymapplication.utils.SecurityConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService implements ILoginAttemptService {

    private final Map<String, BruteForceProtectionData> attemptCache = new HashMap<>();

    public BruteForceProtectionData processUser(String ipUserToken) {
        return getAttempts(ipUserToken);
    }

    @Cacheable(value = "loginAttempts", key = "#ipUserToken", cacheManager = "cacheManager")
    public BruteForceProtectionData getAttempts(String ipUserToken) {
        return attemptCache.getOrDefault(ipUserToken, new BruteForceProtectionData(ipUserToken));
    }

    @CachePut(value = "loginAttempts", key = "#ipUserToken")
    public BruteForceProtectionData updateAttempts(String ipUserToken, BruteForceProtectionData data) {
        attemptCache.put(ipUserToken, data);
        return data;
    }

    @CacheEvict(value = "loginAttempts", key = "#ipUserToken")
    public void clearAttempts(String ipUserToken) {
        attemptCache.remove(ipUserToken);
    }

    public boolean isBlocked(String username, String ip) {
        String ipUserToken = createIpUserToken(username, ip);
        BruteForceProtectionData data = processUser(ipUserToken);
        if (data.getAttempt() >= SecurityConstants.MAX_LOGIN_ATTEMPTS_3) {
            if (System.currentTimeMillis() - data.getLastAttemptTimeMillis() < SecurityConstants.LOCK_TIME_DURATION_5_MIN_IN_MILLIS) {
                return true;
            }
            clearAttempts(ipUserToken);
        }
        return false;
    }

    public void registerAttempt(String username, String ip) {
        String ipUserToken = createIpUserToken(username, ip);
        BruteForceProtectionData data = processUser(ipUserToken);
        data.setAttempt(data.getAttempt() + 1);
        data.setLastAttemptTimeMillis(System.currentTimeMillis());
        updateAttempts(ipUserToken, data);
    }

    public void resetAttempts(String username, String ip) {
        String ipUserToken = createIpUserToken(username, ip);
        clearAttempts(ipUserToken);
    }

    private String createIpUserToken(String username, String ip) {
        return String.format("%s-%s", ip, username);
    }

}
