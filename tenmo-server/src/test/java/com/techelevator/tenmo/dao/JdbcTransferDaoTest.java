package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    @Before
    public void setup() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);
        this.transferDao = new JdbcTransferDao(jdbcTemplate, userDao);
    }

    @Test
    public void transferListReturnsExpectedNumberOfTransfers() {
        insertTestTransfer();
        List<Transfer> retrievedTransferList = transferDao.getTransferList("testUserOne");
        Assert.assertEquals(1, retrievedTransferList.size());
            }

     @Test
     public void expectedTransferReturnsWithCorrectUsers(){
        insertTestTransfer();
        Transfer retrievedTransfer= transferDao.getTransferList("testUserOne").get(0);

        Assert.assertEquals("testUserOne",retrievedTransfer.getUserFrom().getUsername());
        Assert.assertEquals("testUserTwo",retrievedTransfer.getUserTo().getUsername());
//        Assert.assertEquals(retrievedTransfer.getUserFrom(),user1);
//        Assert.assertEquals(retrievedTransfer.getUserTo(),user2);
     }

    private void insertTestTransfer(){
        Transfer testTransfer = new Transfer();
        testTransfer.setAmount(BigDecimal.TEN);
        Account account1 = accountDao.getAccountsByUserName("testUserOne").get(0);
        Account account2 = accountDao.getAccountsByUserName("testUserTwo").get(0);
        account1.setAccountBalance(account1.getAccountBalance().subtract(BigDecimal.TEN));
        account2.setAccountBalance(account2.getAccountBalance().add(BigDecimal.TEN));
        transferDao.executeTransfer(testTransfer, account1, account2);
    }
}
