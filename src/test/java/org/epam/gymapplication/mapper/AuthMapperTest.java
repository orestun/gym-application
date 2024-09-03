package org.epam.gymapplication.mapper;

import org.epam.gymapplication.domain.dto.*;
import org.epam.gymapplication.repository.mapper.AuthMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class AuthMapperTest {
    @InjectMocks
    private AuthMapper authMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapAuthPayloadToAuthDTO() {
        AuthPayload payload = new AuthPayload("testUser", "testPass");

        AuthDTO expectedDTO = AuthDTO.builder()
                .username("testUser")
                .password("testPass")
                .build();
        AuthDTO result = authMapper.mapAuthPayloadToAuthDTO(payload);

        assertEquals(expectedDTO, result);
    }

    @Test
    void testMapChangeLoginPayloadToChangeLoginDTO() {
        ChangeLoginPayload payload = ChangeLoginPayload.builder()
                .username("testUser")
                .password("oldPass")
                .newPassword("newPass")
                .build();

        ChangeLoginDTO expectedDTO = ChangeLoginDTO.builder()
                .username("testUser")
                .password("oldPass")
                .newPassword("newPass")
                .build();
        ChangeLoginDTO result = authMapper.mapChangeLoginPayloadToChangeLoginDTO(payload);
        assertEquals(expectedDTO, result);
    }

    @Test
    void testMapAuthRegistrationDTOToAuthRegistrationPayload() {
        AuthRegistrationDTO registrationDTO = AuthRegistrationDTO.builder()
                .username("testUser")
                .password("testPass")
                .jwtToken("token123")
                .build();

        AuthRegistrationPayload expectedPayload = AuthRegistrationPayload.builder()
                .username("testUser")
                .password("testPass")
                .jwtToken("token123")
                .build();

        AuthRegistrationPayload result = authMapper.mapAuthRegistrationDTOToAuthRegistrationPayload(registrationDTO);

        assertEquals(expectedPayload, result);
    }
}
