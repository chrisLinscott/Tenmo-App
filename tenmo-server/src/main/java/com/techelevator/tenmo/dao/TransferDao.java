package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransferList(String username);

    public void executeTransfer(Transfer transfer, Account accountFrom, Account accountTo);


}
