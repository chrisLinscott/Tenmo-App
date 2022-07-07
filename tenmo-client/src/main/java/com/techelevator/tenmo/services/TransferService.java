package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();


    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String doTransfer(Transfer transfer, AuthenticatedUser authenticatedUser) {
        String url = baseUrl + "transfers";
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> request = new HttpEntity<Transfer>(transfer, headers);
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
            return "Transfer posted";
        } catch (RestClientException e) {
            return e.getMessage();
        }

    }
}