package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TelegramService {
    User getUserByChatId(Long chatId);

    UserDetails getUserDetails(String email);

    void addCargo(CargoDto cargo);

    List<Cargo> getUserCargos();

    String deleteCargo(String cargoId);
}
