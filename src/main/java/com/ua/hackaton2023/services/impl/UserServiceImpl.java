package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.exceptions.UserAlreadyExistsException;
import com.ua.hackaton2023.models.User;
import com.ua.hackaton2023.repository.UserRepository;
import com.ua.hackaton2023.services.UserService;
import com.ua.hackaton2023.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userRepository.save(user);
    }
}
