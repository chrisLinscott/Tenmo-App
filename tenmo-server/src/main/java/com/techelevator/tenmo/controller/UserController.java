package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path="users",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path="user", method = RequestMethod.GET)
    public User getCurrentUser(Principal principal){
        String userName = principal.getName();
        User user = userDao.findByUsername(userName);
        return user;
    }

}
