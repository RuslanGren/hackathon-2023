package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final UserServiceImpl userService;

    public User getUserByChatId(Long chatId) {
       return userService.findByChatId(chatId).get();

    }

}
