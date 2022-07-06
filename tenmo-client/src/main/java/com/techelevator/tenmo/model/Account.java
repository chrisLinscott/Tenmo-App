package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int id;
    private BigDecimal accountBalance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

}
