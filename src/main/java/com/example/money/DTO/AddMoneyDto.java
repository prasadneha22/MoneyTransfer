package com.example.money.DTO;

public class AddMoneyDto {


    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AddMoneyDto{" +
                "amount=" + amount +
                '}';
    }
}
