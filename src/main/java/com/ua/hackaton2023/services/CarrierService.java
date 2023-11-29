package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CarrierService {
    Carrier addCarrier(User user, String name);

    Carrier addCar(CarDto carDto, UserDetails userDetails);

    Carrier addCars(List<CarDto> carDtos, UserDetails userDetails);

    Carrier responseCargo(Long cargoId, CarrierResponseDto carrierResponseDto, UserDetails userDetails);

    void deleteCar(Long carId, UserDetails userDetails);

    Carrier getCarrierByUserDetails(UserDetails userDetails);
}
