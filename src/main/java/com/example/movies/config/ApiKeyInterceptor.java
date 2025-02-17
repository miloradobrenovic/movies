package com.example.movies.config;

import com.example.movies.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestApiKey = request.getParameter("api_key");

        if (requestApiKey == null || requestApiKey.isEmpty()) {
            throw new UnauthorizedException("API key is missing.");
        } else if (!requestApiKey.equals(apiKey)) {
            throw new UnauthorizedException("Invalid API key.");
        }

        return true; // Proceed with the request
    }
}