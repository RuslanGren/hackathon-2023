package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.User;

public interface TelegramService {
    User getUserByChatId(Long chatId);
}
