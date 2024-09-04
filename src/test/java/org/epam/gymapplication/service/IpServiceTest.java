package org.epam.gymapplication.service;

import jakarta.servlet.http.HttpServletRequest;
import org.epam.gymapplication.service.impl.IpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IpServiceTest {

    @Test
    void getIpAddressFromHeader_ValidHeader_ReturnsIpAddress() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of("X-Forwarded-For")));
        when(request.getHeaders("X-Forwarded-For")).thenReturn(Collections.enumeration(List.of("192.168.1.1")));

        String ipAddress = IpService.getIpAddressFromHeader(request);

        assertNotNull(ipAddress);
        assertEquals("192.168.1.1", ipAddress);
    }
}
