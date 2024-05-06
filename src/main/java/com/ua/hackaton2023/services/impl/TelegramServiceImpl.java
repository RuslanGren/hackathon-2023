package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramServiceImpl implements TelegramService {
    private final UserService userService;

    public User getUserByChatId(Long chatId) {
       return userService.findByChatId(chatId).orElseThrow(() -> new BadRequestException("Login Error"));

    }

}
