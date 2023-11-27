package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.user.UserDto;

public interface AuthService {
    User register(UserDto userDto);
}
