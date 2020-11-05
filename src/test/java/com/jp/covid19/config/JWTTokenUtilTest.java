package com.jp.covid19.config;

import com.jp.covid19.exception.CustomException;
import com.jp.covid19.service.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JWTTokenUtilTest {

    @MockBean
    JwtUserDetailsService jwtUserDetailsService;

    JWTTokenUtil tokenUtil = null;

    @BeforeEach()
    public void setup() {
        tokenUtil = new JWTTokenUtil();
        ReflectionTestUtils.setField(tokenUtil, "secret", "asdhkjasweeasd");
        ReflectionTestUtils.setField(tokenUtil, "jwtUserDetailsService", jwtUserDetailsService);
    }

    @Test
    public void testTokenGeneration() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test");
        when(userDetails.getPassword()).thenReturn("test");
        String token = tokenUtil.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));


    }

    @Test
    public void validateTokenSuccessfully() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test");
        when(userDetails.getPassword()).thenReturn("test");
        String token = tokenUtil.generateToken(userDetails);
        assertNotNull(token);

        boolean result = tokenUtil.validateToken(token);
        assertTrue(result);
    }

    @Test
    public void validateTokenWithException() {

        Exception exception = assertThrows(CustomException.class, () -> {
             tokenUtil.validateToken("asdajsdhjasdkjhjkheauaegagd");
        });

        assertNotNull(exception);
        assertEquals("Expired or invalid JWT token", exception.getMessage());

    }

    @Test
    public void testAuthentication() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test");
        when(userDetails.getPassword()).thenReturn("test");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        String token = tokenUtil.generateToken(userDetails);
        when(jwtUserDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = tokenUtil.getAuthentication(token);
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());

    }
}
