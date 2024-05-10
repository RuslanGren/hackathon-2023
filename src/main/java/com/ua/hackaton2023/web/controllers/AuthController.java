package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.services.impl.TelegramServiceImpl;
import com.ua.hackaton2023.web.user.JwtRequest;
import com.ua.hackaton2023.web.user.JwtResponse;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import com.ua.hackaton2023.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final TelegramServiceImpl telegramService;

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam Long chatId) {
        return new ResponseEntity<>(telegramService.getUserByChatId(chatId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthToken(
            @RequestBody JwtRequest authRequest,
            @RequestParam(required = false) Optional<Long> chatId
    ) {
        return new ResponseEntity<>(authService.createAuthToken(authRequest, chatId), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return new ResponseEntity<>(authService.createNewUser(registrationUserDto), HttpStatus.OK);
    }
}
