package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.CarrierResponse;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TelegramService {
    User getUserByChatId(Long chatId);

    UserDetails getUserDetails(String email);

    void addCargo(CargoDto cargo);

    List<Cargo> getUserCargos();

    String deleteCargo(String cargoId);

    void addCar(CarDto carDto);

    String deleteCar(String carId);

    List<Car> getAllCars();

    List<Cargo> getAllCargos();

    void addCarrierResponse(CarrierResponseDto carrierResponseDto);

    List<CarrierResponse> getAllCarrierResponsesByCustomerCargos();
}
