package com.example.money.controller;

import com.example.money.entity.Users;
import com.example.money.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users users){
        System.out.println("receieved users: " + users);
        return userService.register(users);

    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return userService.verify(users);
    }
}
