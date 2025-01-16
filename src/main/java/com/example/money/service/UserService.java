package com.example.money.service;

import com.example.money.entity.Users;
import com.example.money.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users){
        users.setPassword(
                encoder.encode(users.getPassword()));
        return userRepository.save(users);
    }

    public String verify(Users users) {
        try {
            // Authenticate the user using AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(users.getEmail());
            }
        } catch (Exception e) {
            return "fail " + e.getMessage(); // Authentication failed, you can log the exception for more info
        }
        return "fail";
    }




}
