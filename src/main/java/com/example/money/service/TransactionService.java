package com.example.money.service;

import com.example.money.DTO.TransactionResponse;
import com.example.money.entity.UserTranscation;
import com.example.money.entity.Users;
import com.example.money.repository.TransactionRepository;
import com.example.money.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    public String sendMoney(TransactionResponse transactionResponse) {
        Integer senderId = transactionResponse.getSenderId();
        Integer receiverId = transactionResponse.getReceiverId();
        Double amount = transactionResponse.getAmount();

        if(amount <= 0){
            throw  new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Users sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found!"));
        Users receiver = userRepository.findById(receiverId)
                .orElseThrow(()->new RuntimeException("Receiver not found!!"));

        if(sender.getBalance() < amount){
            throw new IllegalArgumentException("Insufficient balance.");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        UserTranscation userTranscation = new UserTranscation();
        userTranscation.setSender(sender);
        userTranscation.setReceiver(receiver);
        userTranscation.setAmount(amount);

        transactionRepository.save(userTranscation);

        return "Transaction successful. Amount: " + amount;
    }


    public List<UserTranscation> getTransactionHistory(Integer userId) {
        return transactionRepository.findBySenderId(userId);
    }
}
