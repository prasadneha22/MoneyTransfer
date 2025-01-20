package com.example.money.controller;

import com.example.money.DTO.TransactionResponse;
import com.example.money.DTO.UserHistoryDto;
import com.example.money.entity.UserTranscation;
import com.example.money.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMoney(@RequestHeader("Authorization") String token, @RequestBody TransactionResponse transactionResponse){

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        try{
            String response = transactionService.sendMoney(token,transactionResponse);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<UserHistoryDto>> getTransactionHistory(@PathVariable Integer userId, @RequestHeader("Authorization") String token){

        try{
            List<UserHistoryDto> transcations = transactionService.getTransactionHistory(userId,token);
            return ResponseEntity.ok(transcations);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
