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
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String result = userService.verify(new Users(null,loginDto.getEmail(),null,loginDto.getPassword(),null));
        if(result.startsWith("fail")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-money")
    public ResponseEntity<String> addMoney(@RequestBody AddMoneyDto addMoneyDto, @RequestHeader("Authorization") String token){
        try{
            String response = userService.addMoney(token, addMoneyDto.getAmount());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
