package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.OK);
    }
}
