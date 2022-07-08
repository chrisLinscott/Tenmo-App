package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Transfer getById(int id) {
        Transfer transfer = null;

        String sql = "select   " +
                "  transfer.transfer_id,   " +
                "  transfer.amount,   " +
                "  user_from.username as user_from,   " +
                "  user_to.username as user_to,  " +
                "  transfer_type.transfer_type_desc,  " +
                "  transfer_status.transfer_status_desc AS status  " +
                "from transfer  " +
                "join account as account_from on transfer.account_from = account_from.account_id  " +
                "join account as account_to on transfer.account_to = account_to.account_id  " +
                "join tenmo_user as user_from on account_from.user_id = user_from.user_id  " +
                "join tenmo_user as user_to on account_to.user_id = user_to.user_id  " +
                "join transfer_type on transfer.transfer_type_id = transfer_type.transfer_type_id  " +
                "join transfer_status on transfer.transfer_status_id = transfer_status.transfer_status_id  " +
                "WHERE transfer.transfer_id = ?  ";


        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);

        if (results.next()){
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    @Override
    public List<Transfer> getTransferList(String username) {
        String sql = "select " +
                "transfer.transfer_id, " +
                "transfer.amount, " +
                "user_from.username as user_from, " +
                "user_to.username as user_to, " +
                "transfer_type.transfer_type_desc, " +
                "transfer_status.transfer_status_desc AS status " +
                "from transfer " +
                "join account as account_from on transfer.account_from = account_from.account_id " +
                "join account as account_to on transfer.account_to = account_to.account_id " +
                "join tenmo_user as user_from on account_from.user_id = user_from.user_id " +
                "join tenmo_user as user_to on account_to.user_id = user_to.user_id " +
                "join transfer_type on transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "join transfer_status on transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE user_from.username = ? or user_to.username = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        List<Transfer> transfers = new ArrayList<>();

        while (results.next()) {
            transfers.add(mapRowToTransfer(results));

        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet row) {
        Transfer transfer = new Transfer();
        transfer.setId(row.getInt("transfer_id"));
        transfer.setTransferType(row.getString("transfer_type_desc"));
        transfer.setTransferStatus(row.getString("status"));
        transfer.setUserFrom(userDao.findByUsername(row.getString("user_from")));
        transfer.setUserTo(userDao.findByUsername(row.getString("user_to")));
        transfer.setAmount(row.getBigDecimal("amount"));

        return transfer;

    }

    @Override
    public void executeTransfer(Transfer transfer, Account accountFrom, Account accountTo) {
        String sql = "INSERT INTO transfer(account_from, account_to, amount, transfer_status_id,transfer_type_id) " +
                "VALUES (?, ?, ?,  2, 2)";

        jdbcTemplate.update(sql, accountFrom.getId(), accountTo.getId(), transfer.getAmount());

        updateBalances(accountFrom, accountTo);
    }


    private void updateBalances(Account accountFrom, Account accountTo) {
        String sql = "UPDATE account SET balance = ? WHERE account_id= ?";

        jdbcTemplate.update(sql, accountFrom.getAccountBalance(), accountFrom.getId());
        jdbcTemplate.update(sql, accountTo.getAccountBalance(), accountTo.getId());
    }

}
