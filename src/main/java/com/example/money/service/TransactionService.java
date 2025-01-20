package com.example.money.service;

import com.example.money.DTO.TransactionResponse;
import com.example.money.DTO.UserHistoryDto;
import com.example.money.entity.UserTranscation;
import com.example.money.entity.Users;
import com.example.money.repository.TransactionRepository;
import com.example.money.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public List<UserHistoryDto> getTransactionHistory(Integer userId, String token) {

        Integer loggedInUserId = jwtService.extractUserId(token.substring(7));

        if(!loggedInUserId.equals(userId)){
            throw new RuntimeException("You do not have permission to view this transaction history.");
        }

        List<UserTranscation> transcations = transactionRepository.findBySenderId(userId);

        return transcations.stream().map(transcation ->{
            Users sender = userRepository.findById(transcation.getSender().getId()).orElseThrow(()->new RuntimeException("Sender not found"));
            Users receiver = userRepository.findById(transcation.getReceiver().getId()).orElseThrow(()->new RuntimeException("User not found"));

            return new UserHistoryDto(
                    sender.getName(),
                    sender.getEmail(),
                    receiver.getName(),
                    receiver.getEmail(),
                    transcation.getAmount(),
                    transcation.getTimestamp()
            );


        }).collect(Collectors.toList());



    }

    public String sendMoney(String token, TransactionResponse transactionResponse) {

       Integer userId = jwtService.extractUserId(token);

        Users sender = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with this id: " + userId));

        Integer receiverId = transactionResponse.getReceiverId();
        if(userId == receiverId){
            throw new IllegalArgumentException("User can't send money to his own account");
        }
        Double amount = transactionResponse.getAmount();

        if(amount <= 0){
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        Users receiver = userRepository.findById(receiverId)
                .orElseThrow(()-> new RuntimeException("Receiver not found!!"));

        if(sender.getBalance() < amount){
            throw new RuntimeException("Insufficient balance.");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        UserTranscation userTranscation = new UserTranscation();
        userTranscation.setSender(sender);
        userTranscation.setReceiver(receiver);
        userTranscation.setAmount(amount);
        userTranscation.setTimestamp(LocalDateTime.now());

        transactionRepository.save(userTranscation);

        return "Transaction successful. Amount: " + amount;

    }
}
