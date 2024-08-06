package com.gomez.docutrack.bundle;

import com.gomez.docutrack.bundle.entities.User;
import com.gomez.docutrack.bundle.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoding() {
        System.out.println("Starting testPasswordEncoding...");
        
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        System.out.println("User created: " + user.getUsername());

        userService.registerUser(user);
        System.out.println("User registered with encoded password.");

        User retrievedUser = userService.findByUsername("testuser");
        System.out.println("Retrieved user: " + (retrievedUser != null ? retrievedUser.getUsername() : "null"));

        assertNotNull(retrievedUser);
        assertTrue(passwordEncoder.matches("testpassword", retrievedUser.getPassword()));

        System.out.println("Password matches: " + passwordEncoder.matches("testpassword", retrievedUser.getPassword()));
        System.out.println("testPasswordEncoding completed.");
    }
}
