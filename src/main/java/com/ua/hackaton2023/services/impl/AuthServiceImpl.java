package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Role;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.user.NotRoleExists;
import com.ua.hackaton2023.exceptions.user.PasswordBadRequestException;
import com.ua.hackaton2023.exceptions.user.UserAlreadyExistsException;
import com.ua.hackaton2023.repository.UserRepository;
import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CarrierService carrierService;

    @Override
    public User register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        if (!userDto.getPassword().equals(userDto.getRepeatedPassword())) {
            throw new PasswordBadRequestException();
        }
        String role = userDto.getRole();
        User user = new User(
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                Role.valueOf(role)
        );
        userRepository.save(user);

        if (role.equals("CUSTOMER")) {
            customerService.addCustomer(user, userDto.getName());
        } else if (role.equals("CARRIER")){
            carrierService.addCarrier(user, userDto.getName());
        } else {
            throw new NotRoleExists();
        }
        return user;
    }
}
