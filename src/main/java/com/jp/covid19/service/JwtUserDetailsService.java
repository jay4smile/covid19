package com.jp.covid19.service;

import com.jp.covid19.domain.ResponseDetails;
import com.jp.covid19.domain.User;
import com.jp.covid19.domain.UserDTO;
import com.jp.covid19.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Service is called");
        Optional<User> user = userRepository.findByUsername(username);
        System.out.println(user.isPresent());
        if (user.isPresent()) {
            User userDetails = user.get();

            return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
                    userDetails.getPassword(), new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public ResponseDetails save(UserDTO user)  {
        boolean userExist = userRepository.existsByUsername(user.getUsername());
ResponseDetails responseDetails = new ResponseDetails();
        if (!userExist) {
            User newUser = new User();
            newUser.setUsername(user.getUsername());

            newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
            userRepository.save(newUser);
            responseDetails.setMessage("User registered successfully!");
        } else {
            responseDetails.setMessage("User already exists!");
        }
        responseDetails.setUser(user);
        return responseDetails;
    }
}
