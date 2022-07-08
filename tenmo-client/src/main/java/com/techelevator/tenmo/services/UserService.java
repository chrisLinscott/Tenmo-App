package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class UserService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    public UserService(String baseUrl) {

        this.baseUrl = baseUrl;
    }

    public List<Account> getAccounts(AuthenticatedUser currentUser) {

        String url = baseUrl + "/user/accounts";

        HttpEntity<Void> request = new HttpEntity<>(makeHeaders(currentUser));

        ResponseEntity<Account[]> response = restTemplate.exchange(url, HttpMethod.GET, request, Account[].class);

        Account[] accounts = response.getBody();

        return Arrays.asList(accounts);

    }
    public List<User> getOtherUsers(AuthenticatedUser currentUser){
        String url = baseUrl + "/other_users";
        HttpEntity<Void> request=new HttpEntity<>(makeHeaders(currentUser));
        ResponseEntity<User[]> response=restTemplate.exchange(url,HttpMethod.GET,request,User[].class);
        return Arrays.asList(response.getBody());
    }

    private HttpHeaders makeHeaders(AuthenticatedUser currentUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
    return headers;
    }

}
