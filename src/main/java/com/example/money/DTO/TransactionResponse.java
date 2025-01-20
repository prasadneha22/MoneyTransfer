package com.example.money.DTO;

import com.example.money.entity.UserTranscation;

import java.time.LocalDateTime;

public class TransactionResponse {


    private Integer receiverId;
    private Double amount;

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "receiverId=" + receiverId +
                ", amount=" + amount +
                '}';
    }

    public TransactionResponse(Integer receiverId, Double amount) {
        this.receiverId = receiverId;
        this.amount = amount;
    }
}

