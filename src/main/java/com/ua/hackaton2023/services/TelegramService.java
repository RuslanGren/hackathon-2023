package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface TelegramService {
    User getUserByChatId(Long chatId);

    UserDetails getUserDetails(User user);
}
