package com.ua.hackaton2023.services;

import com.ua.hackaton2023.web.user.JwtRequest;
import com.ua.hackaton2023.web.user.JwtResponse;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import com.ua.hackaton2023.web.user.UserDto;

import java.util.Optional;

public interface AuthService {
    JwtResponse createAuthToken(JwtRequest authRequest, Optional<Long> chatId);

    UserDto createNewUser(RegistrationUserDto registrationUserDto);
}
