package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.exception.InvalidRecipientException;
import com.techelevator.tenmo.exception.InvalidTransferAmountException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(UserDao userDao, TransferDao transferDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }



    @RequestMapping(path = "transfers", method = RequestMethod.GET)
    public List<Transfer> getUserTransfers(Principal principal) {

        List<Transfer> transfers = transferDao.getTransferList(principal.getName());

        return transfers;
    }

    @RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){

        Transfer transfer = transferDao.getById(id);
        if (transfer == null){
            throw new TransferNotFoundException();
        }
        return transfer;

    }


    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public void transferMoney(@RequestBody Transfer transfer, Principal principal) {
        User sender = userDao.findByUsername(principal.getName());
        Account senderAccount = accountDao.getAccountsByUserName(principal.getName()).get(0);
        transfer.setAccountIdFrom(senderAccount.getId());

        User receiver = userDao.findByUsername(transfer.getUserTo().getUsername());
        Account receiverAccount = accountDao.getAccountsByUserName(receiver.getUsername()).get(0);
        transfer.setAccountIdTo(receiverAccount.getId());

        if (receiver == sender) {
            throw new InvalidRecipientException();
        } else if (senderAccount.getAccountBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientFundsException();
        } else if (transfer.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) { //greater than 0
            throw new InvalidTransferAmountException();
        } else {
            receiverAccount.setAccountBalance(receiverAccount.getAccountBalance().add(transfer.getAmount()));
            senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(transfer.getAmount()));
            transferDao.executeTransfer(transfer, receiverAccount, senderAccount);
        }

    }

    private void checkIfValid(Transfer transfer) {

    }

}
