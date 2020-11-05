package com.jp.covid19.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class CovidDetailService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.covid19.url.statewise}")
    private String stateWiseUrl;

    @Value("${api.covid19.url.timeseries}")
    private String timeSeriesUrl;

    public String getStateWiseDetails() {
        String response = restTemplate.getForObject(stateWiseUrl,String.class);
        return response;
    }

    public String getTimeSeriesDetails() {
        String response = restTemplate.getForObject(timeSeriesUrl,String.class);
        return response;
    }

}
