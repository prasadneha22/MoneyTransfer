package com.example.money.service;

import com.example.money.DTO.LoginDto;
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

import java.util.HashMap;
import java.util.Map;

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




    public String addMoney(String token, Double amount) {
        if(amount <=0){
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        Integer userId = jwtService.extractUserId(token.substring(7));
        Users user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found!!"));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        return "Money added to your account successfully. New Balance: " + user.getBalance();


    }

    public Double getBalance(String token) {
        Integer id = jwtService.extractUserId(token);

        Users user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found!!"));

        return user.getBalance();

    }

    public Map<String, Object> verify(LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            if (authentication.isAuthenticated()) {
                Users user = userRepository.findByEmail(loginDto.getEmail());
                if (user == null) {
                    response.put("error", "User not found!");
                    return response;
                }

                // Generate JWT token with user details
                String token = jwtService.generateToken(user);

                // Return user details and token
                response.put("id", user.getId());
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("balance", user.getBalance());
                response.put("token", token);
                return response;
            }
        } catch (Exception e) {
            response.put("error", "Invalid credentials!");
        }

        return response;
    }
//


}
