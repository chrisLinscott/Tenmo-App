package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();


    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String doTransfer(Transfer transfer, AuthenticatedUser authenticatedUser) {
        String url = baseUrl + "transfers";

        HttpEntity<Transfer> request = new HttpEntity<Transfer>(transfer, makeHeaders(authenticatedUser));
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
            return "Transfer posted";
        } catch (RestClientException e) {
            return e.getMessage();
        }

    }

    public Transfer getTransferById(int id, AuthenticatedUser authenticatedUser) {

        String url = baseUrl + "/transfers/" + id;

        HttpEntity<Void> request = new HttpEntity<>(makeHeaders(authenticatedUser));

        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, request, Transfer.class);
            transfer = response.getBody();

        } catch (HttpClientErrorException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfer;

    }

    public List<Transfer> getTransfers(AuthenticatedUser authenticatedUser) {

        String url = baseUrl + "/transfers";

        HttpEntity<Void> request = new HttpEntity<>(makeHeaders(authenticatedUser));
        ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, request,
                                                                    Transfer[].class
                                                                   );

        Transfer[] transfers = response.getBody();

        return Arrays.asList(transfers);

    }

    private HttpHeaders makeHeaders(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}