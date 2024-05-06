package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.user.RegistrationUserDto;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    User createNewUser(RegistrationUserDto registrationUserDto);

    void addChatId(String email, Long chatId);

    Optional<User> findByChatId(Long chatId);
}
