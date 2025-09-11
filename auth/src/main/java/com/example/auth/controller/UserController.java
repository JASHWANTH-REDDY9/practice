package com.example.auth.controller;

import com.example.auth.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/api/user/home")
    public Map<String, String> home(HttpServletRequest request) {
        // user attribute set by JwtFilter if valid token provided
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return Map.of("message", "Guest");
        }
        return Map.of("message", "Welcome " + user.getUsername(), "email", user.getEmail());
    }
}
