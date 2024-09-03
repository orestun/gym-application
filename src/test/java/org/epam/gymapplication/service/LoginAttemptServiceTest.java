package org.epam.gymapplication.service;

import org.epam.gymapplication.service.impl.LoginAttemptService;
import org.epam.gymapplication.utils.BruceForceProtectionData;
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
        BruceForceProtectionData expectedData = new BruceForceProtectionData(username);
        expectedData.setAttempt(1);

        loginAttemptService.updateAttempts(username, expectedData);
        BruceForceProtectionData result = loginAttemptService.processUser(username);

        assertEquals(expectedData, result);
    }

    @Test
    public void testProcessUser_WhenUserDoesNotExist() {
        String username = "newUser";

        BruceForceProtectionData result = loginAttemptService.processUser(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testGetAttempts_WhenUserExists() {
        String username = "existingUser";
        BruceForceProtectionData expectedData = new BruceForceProtectionData(username);
        expectedData.setAttempt(2);

        loginAttemptService.updateAttempts(username, expectedData);

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(expectedData, result);
    }

    @Test
    public void testGetAttempts_WhenUserDoesNotExist() {
        String username = "nonExistingUser";

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testUpdateAttempts() {
        String username = "userToUpdate";
        BruceForceProtectionData data = new BruceForceProtectionData(username);
        data.setAttempt(3);

        loginAttemptService.updateAttempts(username, data);

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(3, result.getAttempt());
    }

    @Test
    public void testClearAttempts() {
        String username = "userToClear";
        BruceForceProtectionData data = new BruceForceProtectionData(username);
        data.setAttempt(2);

        loginAttemptService.updateAttempts(username, data);
        loginAttemptService.clearAttempts(username);

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(0, result.getAttempt());
    }

    @Test
    public void testIsBlocked_UserNotBlocked() {
        String username = "userNotBlocked";
        BruceForceProtectionData data = new BruceForceProtectionData(username);
        data.setAttempt(2);

        loginAttemptService.updateAttempts(username, data);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testIsBlocked_UserBlocked() {
        String username = "userBlocked";
        BruceForceProtectionData data = new BruceForceProtectionData(username);
        data.setAttempt(SecurityConstants.MAX_LOGIN_ATTEMPTS_3_FOR_SPECIFIC_TIME);
        data.setLastAttemptTimeMillis(System.currentTimeMillis());

        loginAttemptService.updateAttempts(username, data);

        assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testRegisterAttempt() {
        String username = "userRegisterAttempt";

        loginAttemptService.registerAttempt(username);

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(1, result.getAttempt());
    }

    @Test
    public void testResetAttempts() {
        String username = "userResetAttempts";
        BruceForceProtectionData data = new BruceForceProtectionData(username);
        data.setAttempt(3);

        loginAttemptService.updateAttempts(username, data);
        loginAttemptService.resetAttempts(username);

        BruceForceProtectionData result = loginAttemptService.getAttempts(username);

        assertEquals(0, result.getAttempt());
    }

}
