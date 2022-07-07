package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
    public void executeTransfer(Transfer transfer, Account accountFrom, Account accountTo);
}
