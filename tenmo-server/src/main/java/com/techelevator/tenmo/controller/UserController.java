package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path="other_users",method = RequestMethod.GET)
    public List<User> getOtherUsers(Principal principal){
        List<User> users= userDao.findAll();
        for(int i =0;i<users.size();i++){
           User user=users.get(i);
           if(user.getUsername()==principal.getName()){
               users.remove(i);
           }else {
               user.setAccountList(null);
           }
        }
                return users;
    }

    @RequestMapping(path="user", method = RequestMethod.GET)
    public User getCurrentUser(Principal principal){
        String userName = principal.getName();
        User user = userDao.findByUsername(userName);
        return user;
    }

}
