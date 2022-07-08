package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {

    private UserDao userDao;
    private AccountDao accountDao;
    public UserController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao=accountDao;
    }

    @RequestMapping(path="other_users",method = RequestMethod.GET)
    public List<User> getOtherUsers(Principal principal){
        List<User> users= userDao.findAll();
        User currentUser= null;
        for(int i =0;i<users.size();i++){
           User user=users.get(i);
           if(user.getUsername().equalsIgnoreCase(principal.getName())){
               currentUser=user;
           }

        }
        users.remove(currentUser);
                return users;
    }


        @RequestMapping(path="user/accounts", method = RequestMethod.GET)
    public List<Account> getUserAccounts(Principal principal){

            return accountDao.getAccountsByUserName(principal.getName());
        }
}
