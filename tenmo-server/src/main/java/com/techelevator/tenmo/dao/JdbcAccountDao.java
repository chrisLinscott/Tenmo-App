package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAccountsByUserName(String userName) {
            List<Account> accountList= new ArrayList<Account>();
            String sql= "SELECT account.account_id, account.balance " +
                    "FROM account " +
                    "JOIN tenmo_user on tenmo_user.user_id=account.user_id " +
                    "WHERE tenmo_user.username=?";

            SqlRowSet results= jdbcTemplate.queryForRowSet(sql,userName);
            while (results.next()){
                Account account= new Account();
                account.setId(results.getInt("account_id"));
                account.setAccountBalance(results.getBigDecimal("balance"));
                accountList.add(account);
            }
            return accountList;
        }



    }

