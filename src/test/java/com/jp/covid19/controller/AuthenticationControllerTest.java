package com.jp.covid19.controller;


import com.jp.covid19.config.JWTTokenUtil;
import com.jp.covid19.domain.ResponseDetails;
import com.jp.covid19.domain.UserDTO;
import com.jp.covid19.service.JwtUserDetailsService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = AuthenticationController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {


    MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JWTTokenUtil tokenUtil;

    @MockBean
    JwtUserDetailsService userDetailsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegistrationWithInvalidInput() throws Exception {

    mockMvc.perform(post("/user/registration")
            .contentType("application/json")
            .content("{\"username\": \"data\"}")
    ).andExpect(status().is4xxClientError());
    }

    @Test
    public void testRegistrationWithValidInput() throws Exception {
        ResponseDetails responseDetails = new ResponseDetails();
        responseDetails.setMessage("User registered successfully");
        when(userDetailsService.save(any())).thenReturn(responseDetails);
        mockMvc.perform(post("/user/registration")
                .contentType("application/json")
                .content("{\"username\": \"data\",\"password\": \"data\"}")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    public void testLogin() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(tokenUtil.generateToken(any())).thenReturn("eyadcjashjdkhjaksjdkjasd.ajsahdjasd");

        mockMvc.perform(post("/user/login")
                .contentType("application/json")
                .content("{\"username\": \"data\",\"password\": \"data\"}")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Authentication successful!"));
    }

    @Test
    public void testLoginWithError() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Authentication error") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        });
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);


        mockMvc.perform(post("/user/login")
                .contentType("application/json")
                .content("{\"username\": \"data\",\"password\": \"data\"}")
        ).andExpect(status().is4xxClientError());
    }
}
