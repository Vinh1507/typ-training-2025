package com.fintrack.backend.budget.controller;

public class AddSpentRequest {
    private Double amount;
    private String month;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
}
