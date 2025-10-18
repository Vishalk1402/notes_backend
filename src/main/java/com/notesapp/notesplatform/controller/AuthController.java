package com.notesapp.notesplatform.controller;


import com.notesapp.notesplatform.dto.RegisterRequest;
import com.notesapp.notesplatform.dto.LoginRequest;
import com.notesapp.notesplatform.dto.AuthResponse;
import com.notesapp.notesplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    
    
}
