package com.jp.covid19.controller;

import com.jp.covid19.config.JWTTokenUtil;
import com.jp.covid19.domain.ResponseDetails;
import com.jp.covid19.domain.UserDTO;
import com.jp.covid19.exception.CustomException;
import com.jp.covid19.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/***
 * Class: AuthenticationController
 *
 * Desc: Controller class to handle User specific request like Authentication and registration
 */
@RestController
@RequestMapping("/user/")
public class AuthenticationController {


    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenUtil tokenUtil;

    @CrossOrigin()
    @PostMapping(path = "registration")
    public ResponseEntity<ResponseDetails> registerUser(@RequestBody UserDTO user) throws Exception {

        ResponseDetails responseDetails = new ResponseDetails();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            responseDetails.setMessage("Please enter valid input");
            return new ResponseEntity<>(responseDetails, HttpStatus.BAD_REQUEST);
        } else{
            responseDetails = userDetailsService.save(user);
        }


        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @CrossOrigin()
    @PostMapping(path = "login")
    public ResponseEntity<ResponseDetails> login(@RequestBody UserDTO user) throws Exception {
        ResponseDetails responseDetails = new ResponseDetails();

    try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

         String details = tokenUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));


        responseDetails.setToken(details);
        responseDetails.setMessage("Authentication successful!");
    }catch (AuthenticationException ex) {
        ex.printStackTrace();
        responseDetails.setMessage("Unable to authenticate user!");
        throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    responseDetails.setUser(user);
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }
}
