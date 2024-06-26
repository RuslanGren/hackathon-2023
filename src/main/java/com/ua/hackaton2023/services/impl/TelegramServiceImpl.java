package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.CarrierResponse;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final UserServiceImpl userService;
    private final CustomerService customerService;
    private final CarrierService carrierService;
    private final CargoService cargoService;

    @Override
    public User getUserByChatId(Long chatId) {
       return userService.findByChatId(chatId).orElseThrow(() -> new BadRequestException("Error"));
    }

    @Override
    public UserDetails getUserDetails(String email) {
        return userService.loadUserByUsername(email);
    }

    @Override
    public void addCargo(CargoDto cargo) {
        customerService.addCargo(cargo);
    }

    @Override
    public List<Cargo> getUserCargos() {
        return customerService.getCargosByUser(customerService.getCustomer().getId());
    }

    @Override
    public String deleteCargo(String cargoId) {
        try {
            customerService.deleteCargo(Long.parseLong(cargoId));
            return "Груз успішно видалений";
        } catch (Exception e) {
            return "Виникла помилка, попробуйте ще раз";
        }
    }

    @Override
    public void addCar(CarDto carDto) {
        carrierService.addCar(carDto);
    }

    @Override
    public String deleteCar(String carId) {
        try {
            carrierService.deleteCar(Long.parseLong(carId));
            return "Машина успішно видалена";
        } catch (Exception e) {
            return "Виникла помилка, попробуйте ще раз";
        }
    }

    @Override
    public List<Car> getAllCars() {
        return carrierService.getUserCars();
    }

    @Override
    public List<Cargo> getAllCargosIsActive() {
        return cargoService.getAllIsActive();
    }

    @Override
    public String addCarrierResponse(CarrierResponseDto carrierResponseDto) {
        try {
            carrierService.responseCargo(carrierResponseDto);
            return "Відгук після перевезення вантажу успішно відправлений";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Помилка вводу, попробуйте ще раз (Можливо ви ввели не існуючий ID вантажу)";
        }
    }

    @Override
    public List<CarrierResponse> getAllCarrierResponsesByCustomerCargos() {
        return customerService.getAllCarrierResponsesByCustomerCargos();
    }

    @Override
    public String chooseCargoCarrier(String[] data) {
        try {
            Long cargoId = Long.parseLong(data[1]);
            Long carrierResponseId = Long.parseLong(data[2]);
            customerService.chooseCargoCarrier(cargoId, carrierResponseId);
            return "Ви успішно прийняли відгук перевізника на ваш груз";
        } catch (Exception exception) {
            return "Помилка: " + exception.getMessage();
        }
    }

    @Override
    public String finishCargo(String[] data) {
        try {
            Long cargoId = Long.parseLong(data[1]);
            int stars = Integer.parseInt(data[2]);
            customerService.finishCargo(cargoId, stars);
            return "Ви успішно завершили груз та оцінили перевізника на " + stars + " зірок";
        } catch (Exception exception) {
            return "Помилкка:" + exception.getMessage();
        }
    }
}
