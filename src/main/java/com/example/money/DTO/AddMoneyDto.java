package com.example.money.DTO;

public class AddMoneyDto {

    private Integer email;
    private Double amount;

    public AddMoneyDto(){

    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AddMoneyDto{" +
                "email=" + email +
                ", amount=" + amount +
                '}';
    }
}
