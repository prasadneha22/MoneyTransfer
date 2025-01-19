package com.example.money.controller;

import com.example.money.DTO.TransactionResponse;
import com.example.money.entity.UserTranscation;
import com.example.money.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMoney(@RequestBody TransactionResponse transactionResponse){
        try{
            String response = transactionService.sendMoney(transactionResponse);
            return ResponseEntity.ok(response);

        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<UserTranscation>> getTransactionHistory(@PathVariable Integer userId){
        List<UserTranscation> transactions = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }
}
