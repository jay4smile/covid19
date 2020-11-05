package com.jp.covid19.service;


import com.jp.covid19.domain.ResponseDetails;
import com.jp.covid19.domain.User;
import com.jp.covid19.domain.UserDTO;
import com.jp.covid19.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JwtUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService();

    @BeforeEach()
    public void setup() {

    }

    @Test()
    public void testWhenUserDoesNotPresent() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            jwtUserDetailsService.loadUserByUsername("test");
        });
        assertEquals("User not found with username: test", exception.getMessage());
    }

    @Test()
    public void testWhenUserDoesPresent() {
        User userDetail = new User();
        userDetail.setUsername("test");
        userDetail.setPassword("test");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userDetail));

        UserDetails user = jwtUserDetailsService.loadUserByUsername("test");
        assertEquals("test", user.getUsername());
    }


    @Test()
    public void testRegistrationWhenUserAlreadyPresent() {
        UserDTO user = mock(UserDTO.class);
        when(user.getUsername()).thenReturn("test");
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        ResponseDetails responseDetails = jwtUserDetailsService.save(user);

        assertEquals("User already exists!", responseDetails.getMessage());
    }

    @Test()
    public void testRegistrationWhenUserIsNotPresent() {
        UserDTO user = mock(UserDTO.class);
        when(user.getUsername()).thenReturn("test");
        when(user.getPassword()).thenReturn("test");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        ResponseDetails responseDetails = jwtUserDetailsService.save(user);

        assertEquals("User registered successfully!", responseDetails.getMessage());
    }
}
