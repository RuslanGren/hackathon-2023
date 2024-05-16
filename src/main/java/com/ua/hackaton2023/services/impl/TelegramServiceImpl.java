package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final UserServiceImpl userService;
    private final CustomerService customerService;

    @Override
    @Transactional
    public User getUserByChatId(Long chatId) {
       return userService.findByChatId(chatId).orElseThrow(() -> new BadRequestException("Error"));
    }

    @Override
    @Transactional
    public UserDetails getUserDetails(User user) {
        return userService.loadUserByUsername(user.getEmail());
    }

    @Override
    @Transactional
    public void addCargo(CargoDto cargo) {
        customerService.addCargo(cargo);
    }
}
