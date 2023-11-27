package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.UserAlreadyExistsException;
import com.ua.hackaton2023.repository.UserRepository;
import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword())
        );
        userRepository.save(user);
        return user;
    }
}
