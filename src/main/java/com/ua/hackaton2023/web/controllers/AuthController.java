package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.web.user.JwtRequest;
import com.ua.hackaton2023.web.user.JwtResponse;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import com.ua.hackaton2023.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest) {
        return new ResponseEntity<>(authService.createAuthToken(authRequest), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return new ResponseEntity<>(authService.createNewUser(registrationUserDto), HttpStatus.OK);
    }

    @GetMapping("/ns")
    public ResponseEntity<String> testNot() {
        return new ResponseEntity<>("everything is ok NOT SECURED", HttpStatus.OK);
    }

    @GetMapping("/s")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("everything is ok SECURED", HttpStatus.OK);
    }
}
