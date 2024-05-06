package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.exceptions.UnauthorizedException;
import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.util.JwtTokenUtils;
import com.ua.hackaton2023.web.user.JwtRequest;
import com.ua.hackaton2023.web.user.JwtResponse;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import com.ua.hackaton2023.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public JwtResponse createAuthToken(JwtRequest authRequest, Optional<Long> chatId) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                    authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Email or password is wrong");
        }

        chatId.ifPresent(id -> userService.addChatId(authRequest.getEmail(), id)); // if user login via telegram

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.createToken(userDetails);

        return new JwtResponse(token);
    }

    @Override
    public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        User user = userService.createNewUser(registrationUserDto);
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
