package org.epam.gymapplication.service;

import org.epam.gymapplication.service.impl.LoginAttemptService;
import org.epam.gymapplication.utils.BruteForceProtectionData;
import org.epam.gymapplication.utils.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class LoginAttemptServiceTest {

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessUser_WhenUserExists() {
        String username = "testUser";
        BruteForceProtectionData expectedData = new BruteForceProtectionData(username);
        expectedData.setAttempt(1);

        loginAttemptService.updateAttempts(username, expectedData);
        BruteForceProtectionData result = loginAttemptService.processUser(username);

        assertEquals(expectedData, result);
    }

    @Test
    public void testProcessUser_WhenUserDoesNotExist() {
        String ipUserToken = "2345:0425:2CA1:0000:0000:0567:5673:23b5-User";

        BruteForceProtectionData result = loginAttemptService.processUser(ipUserToken);

        assertNotNull(result);
        assertEquals(ipUserToken, result.getIpUserToken());
        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testGetAttempts_WhenUserExists() {
        String username = "existingUser";
        BruteForceProtectionData expectedData = new BruteForceProtectionData(username);
        expectedData.setAttempt(2);

        loginAttemptService.updateAttempts(username, expectedData);

        BruteForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(expectedData, result);
    }

    @Test
    public void testGetAttempts_WhenUserDoesNotExist() {
        String username = "nonExistingUser";

        BruteForceProtectionData result = loginAttemptService.getAttempts(username);

        assertNotNull(result);
        assertEquals(username, result.getIpUserToken());
        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testUpdateAttempts() {
        String username = "userToUpdate";
        BruteForceProtectionData data = new BruteForceProtectionData(username);
        data.setAttempt(3);

        loginAttemptService.updateAttempts(username, data);

        BruteForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(3, result.getAttempt());
    }

    @Test
    public void testClearAttempts() {
        String username = "userToClear";
        BruteForceProtectionData data = new BruteForceProtectionData(username);
        data.setAttempt(2);

        loginAttemptService.updateAttempts(username, data);
        loginAttemptService.clearAttempts(username);

        BruteForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testIsBlocked_UserNotBlocked() {
        String username = "userNotBlocked";
        String ip = "172.32.32.4";
        String token = ip + "-" + username;
        BruteForceProtectionData data = new BruteForceProtectionData(token);
        data.setAttempt(2);

        loginAttemptService.updateAttempts(token, data);

        assertFalse(loginAttemptService.isBlocked(token, ip));
    }

    @Test
    public void testIsBlocked_UserBlocked() {
        String username = "userNotBlocked";
        String ip = "172.32.32.4";
        String token = ip + "-" + username;
        BruteForceProtectionData data = new BruteForceProtectionData(token);
        data.setAttempt(SecurityConstants.MAX_LOGIN_ATTEMPTS_3);
        data.setLastAttemptTimeMillis(System.currentTimeMillis());

        loginAttemptService.updateAttempts(token, data);

        assertTrue(loginAttemptService.isBlocked(username, ip));
    }

    @Test
    public void testRegisterAttempt() {
        String username = "userRegisterAttempt";
        String ip = "172.32.32.4";
        String token = ip + "-" + username;

        loginAttemptService.registerAttempt(username, ip);

        BruteForceProtectionData result = loginAttemptService.getAttempts(token);

        assertEquals(1, result.getAttempt());
    }

    @Test
    public void testResetAttempts() {
        String username = "userRegisterAttempt";
        String ip = "172.32.32.4";
        String token = ip + "-" + username;

        BruteForceProtectionData data = new BruteForceProtectionData(token);
        data.setAttempt(3);

        loginAttemptService.updateAttempts(token, data);
        loginAttemptService.resetAttempts(username, ip);

        BruteForceProtectionData result = loginAttemptService.getAttempts(token);

        assertEquals(0, result.getAttempt());
    }

}
