package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.carrier.CarDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CarrierService {
    Carrier addCarrier(User user);

    Carrier addCar(CarDto carDto, UserDetails userDetails);

    Carrier addCars(List<CarDto> carDtos, UserDetails userDetails);

    Carrier pickCargo(Long cargoId, UserDetails userDetails);

    void deleteCar(Long carId, UserDetails userDetails);

    Carrier getCarrierByUserDetails(UserDetails userDetails);
}
