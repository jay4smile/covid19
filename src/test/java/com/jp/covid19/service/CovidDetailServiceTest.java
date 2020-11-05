package com.jp.covid19.service;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class CovidDetailServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    CovidDetailService covidDetailService =  new CovidDetailService();;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(covidDetailService, "stateWiseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(covidDetailService, "timeSeriesUrl", "http://localhost:8080");
    }

    @Test
    public void testStatewiseDetails() {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn("{\"user\": \"data\"}");
        String response = covidDetailService.getStateWiseDetails();
        Assertions.assertThat(response).isNotEmpty();
    }

    @Test
    public void testNullStatewiseDetails() {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
        String response = covidDetailService.getStateWiseDetails();
        Assertions.assertThat(response).isNull();
    }

    @Test
    public void testTimeDetails() {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn("{\"user\": \"data\"}");
        String response = covidDetailService.getTimeSeriesDetails();
        Assertions.assertThat(response).isNotEmpty();
    }

    @Test
    public void testNullTimeewiseDetails() {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
        String response = covidDetailService.getTimeSeriesDetails();
        Assertions.assertThat(response).isNull();
    }
}
