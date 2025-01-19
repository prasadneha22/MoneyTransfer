package com.example.money.service;

import com.example.money.DTO.UserDto;
import com.example.money.entity.Users;
import com.example.money.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(UserDto userDto){
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(encoder.encode(userDto.getPassword()));

        return userRepository.save(user);
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

    public String addMoney(String token, Double amount) {
        if(amount <=0){
            throw new IllegalArgumentException("AMount must be greater than zero.");
        }

        String email = jwtService.extractEmail(token.substring(7));

        Users user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found!!");
        }

        user.setBalance(user.getBalance() + amount);

        userRepository.save(user);

        return "Money added to your Account Successfully. New Balance: " + user.getBalance();
    }
//


}
