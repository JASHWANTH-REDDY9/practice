package com.example.auth.controller;

import com.example.auth.model.User;
import com.example.auth.payload.LoginRequest;
import com.example.auth.payload.RegisterRequest;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }
        User u = new User(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    

    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest req) {
            var opt = userRepository.findByEmail(req.getEmail());
            if (opt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
            User u = opt.get();

            if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

            String token = jwtUtil.generateToken(u.getEmail());
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("username", u.getUsername());
            return ResponseEntity.ok(body);
        }

}
