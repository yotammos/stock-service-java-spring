package com.mosscorp.stockservice.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosscorp.stockservice.client.UserClient;
import com.mosscorp.stockservice.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class LocalUserClient implements UserClient {

    private static final String LOCAL_ADDRESS = "http://localhost:8081";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public LocalUserClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean validateUser(String userId) {
        final HttpRequest request = HttpRequest.newBuilder(URI.create(LOCAL_ADDRESS + "/user/" + userId)).GET().build();
        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            mapper.readValue(response.body(), User.class); // throws not found exception if user doesn't exist
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
