package com.gomez.docutrack.bundle.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Your account has been disabled. Please contact support.";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "Your account has expired. Please contact support.";
        } else if (exception.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = "You have been blocked due to multiple failed login attempts.";
        }

        System.out.println("Authentication failed: " + exception.getMessage());

        setDefaultFailureUrl("/login?error=true&message=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
