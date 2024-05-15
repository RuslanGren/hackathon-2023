package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface TelegramService {
    User getUserByChatId(Long chatId);

    UserDetails getUserDetails(User user);

    void addCargo(CargoDto cargo, UserDetails userDetails);
}
