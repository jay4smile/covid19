package com.jp.covid19.controller;



import com.jp.covid19.service.CovidDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/covidinformation")
public class CovidDetailController {


    @Autowired
    CovidDetailService covidDetailService;

    @CrossOrigin()
    @GetMapping(path = "/bystate",  produces = "application/json")
    public  String getCovidDetailsByState() {

        String response = covidDetailService.getStateWiseDetails();
        return response;
    }

}
