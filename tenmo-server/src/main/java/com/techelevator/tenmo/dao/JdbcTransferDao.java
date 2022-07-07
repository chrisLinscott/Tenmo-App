package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void executeTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer(account_from, account_to, amount, transfer_status_id,transfer_type_id) " +
                "VALUES (?, ?, ?,  2, 2)";

        jdbcTemplate.update(sql,transfer.getAccountIdFrom(),transfer.getAccountIdTo(),transfer.getAmount());

        updateBalances(transfer);
    }

    private void updateBalances(Transfer transfer){
        String sql = "UPDATE account SET balance = ?";

        jdbcTemplate.update(sql,transfer.getAccountIdFrom())
    }
}
