package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void executeTransfer(Transfer transfer, Account accountFrom, Account accountTo) {
        String sql = "INSERT INTO transfer(account_from, account_to, amount, transfer_status_id,transfer_type_id) " +
                "VALUES (?, ?, ?,  2, 2)";

        jdbcTemplate.update(sql,accountFrom.getId(), accountTo.getId(), transfer.getAmount());

        updateBalances(accountFrom, accountTo);
    }

    private void updateBalances(Account accountFrom, Account accountTo){
        String sql = "UPDATE account SET balance = ? WHERE account_id= ?";

        jdbcTemplate.update(sql, accountFrom.getAccountBalance(), accountFrom.getId());
        jdbcTemplate.update(sql, accountTo.getAccountBalance(),accountTo.getId());
    }
}
