package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public UserDetails getUserDetails(String email) {
        return userService.loadUserByUsername(email);
    }

    @Override
    @Transactional
    public void addCargo(CargoDto cargo) {
        customerService.addCargo(cargo);
    }

    @Override
    @Transactional
    public List<Cargo> getUserCargos() {
        return customerService.getCargosByUser(customerService.getCustomer().getId());
    }

    @Transactional
    public String deleteCargo(String cargoId) {
        try {
            customerService.deleteCargo(Long.parseLong(cargoId));
            return "Груз успішно видалений";
        } catch (Exception e) {
            return "Виникла помилка, попробуйте ще раз";
        }
    }
}
