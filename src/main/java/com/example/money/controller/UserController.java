package com.example.money.controller;

import com.example.money.DTO.AddMoneyDto;
import com.example.money.DTO.LoginDto;
import com.example.money.DTO.UserDto;
import com.example.money.entity.Users;
import com.example.money.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody UserDto userDto){
        Users registerUser = userService.register(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login (@RequestBody LoginDto loginDto){
        Map<String,Object> response = userService.verify(loginDto);
        if(response.containsKey("Error")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-money")
    public ResponseEntity<String> addMoney(@RequestBody AddMoneyDto addMoneyDto, @RequestHeader("Authorization") String token){

        if(addMoneyDto.getAmount() == null || addMoneyDto.getAmount() <= 0){
            return ResponseEntity.badRequest().body("Amount must be greater than zero and not null.");
        }
        try{
            String response = userService.addMoney(token, addMoneyDto.getAmount());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getMyBalance(@RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        Double balance = userService.getBalance(token);

        return ResponseEntity.ok(balance);
    }



}
