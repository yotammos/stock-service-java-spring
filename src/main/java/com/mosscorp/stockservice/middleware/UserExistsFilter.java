package com.mosscorp.stockservice.middleware;

import com.mosscorp.stockservice.client.UserClient;
import com.mosscorp.stockservice.client.impl.LocalUserClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserExistsFilter extends OncePerRequestFilter {

    private final UserClient userClient;

    public UserExistsFilter() {
        this.userClient = new LocalUserClient();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();
        System.out.println("path = " + path);

        final String[] parts = path.split("/");
        if (parts.length < 3 || !parts[1].equals("stocks")) {
            throw new RuntimeException("Invalid request path!");
        }

        final String userId = parts[2];
        System.out.println("user ID = " + userId);

        // validate user
        final boolean doesUserExist = userClient.validateUser(userId);
        if (!doesUserExist) {
            throw new RuntimeException("User not found!");
        }

        // execute the rest of the filters
        filterChain.doFilter(request, response);
    }
}
