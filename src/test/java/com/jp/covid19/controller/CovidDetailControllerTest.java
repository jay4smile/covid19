package com.jp.covid19.controller;

import com.jp.covid19.service.CovidDetailService;
import com.jp.covid19.service.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CovidDetailControllerTest {

    MockMvc mockMvc;


    @MockBean
    CovidDetailService covidDetailService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @WithMockUser(value = "spring")
    @Test
    public void testByStateApi() throws Exception {
        when(covidDetailService.getStateWiseDetails()).thenReturn("{\"user\": \"data\"}");
        this.mockMvc.perform(get("/api/covidinformation/bystate"))
                .andExpect(status().isOk());
    }

    @Test
    public void testByStateApiWithoutAuthentication() throws Exception {
        when(covidDetailService.getStateWiseDetails()).thenReturn("{\"user\": \"data\"}");
        this.mockMvc.perform(get("/api/covidinformation/bystate"))
                .andExpect(status().is4xxClientError());
    }
}
