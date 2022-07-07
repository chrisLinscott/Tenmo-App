package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.exception.InvalidRecipientException;
import com.techelevator.tenmo.exception.InvalidTransferAmountException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public void transferMoney(@RequestBody Transfer transfer, Principal principal) {
        User sender = userDao.findByUsername(transfer.getUserNameFrom());
        Account senderAccount = sender.getAccountList().get(0);
        transfer.setAccountIdFrom(senderAccount.getId());

        User receiver = userDao.findByUsername(transfer.getUserNameTo());
        Account receiverAccount = receiver.getAccountList().get(0);
        transfer.setAccountIdTo(receiverAccount.getId());

        if (receiver == sender) {
            throw new InvalidRecipientException();
        } else if (senderAccount.getAccountBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientFundsException();
        } else if (transfer.getAmount().compareTo(BigDecimal.valueOf(0)) > 0) { //greater than 0
            throw new InvalidTransferAmountException();
        } else {
            receiverAccount.setAccountBalance(receiverAccount.getAccountBalance().add(transfer.getAmount()));
            senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(transfer.getAmount()));
            transferDao.executeTransfer(transfer);
        }

    }

    private void checkIfValid(Transfer transfer) {

    }

}
