package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class UserService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Account> getAccounts(AuthenticatedUser currentUser) {

        String url = baseUrl + "/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());

        HttpEntity<BigDecimal> request = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, request, User.class);

        User user = response.getBody();

        return user.getAccountList();

    }

}
