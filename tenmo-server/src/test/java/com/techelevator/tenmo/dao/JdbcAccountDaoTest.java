package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcAccountDaoTest extends BaseDaoTests{
    private AccountDao accountDao;




      @Before
   public void setup() {
          JdbcTemplate jdbcTemplate=new JdbcTemplate(this.dataSource);
          this.accountDao = new JdbcAccountDao(jdbcTemplate);
      }

    @Test
    public void getAccountsReturnsExpectedNumberOfAccounts (){
        List<Account> accountList=accountDao.getAccountsByUserName("testUserOne");
        Assert.assertEquals(1, accountList.size());
    }



}
