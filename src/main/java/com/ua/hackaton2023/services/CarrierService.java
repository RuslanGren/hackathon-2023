package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CarrierService {
    Carrier addCarrier(User user, String name);

    Carrier addCar(CarDto carDto);

    Carrier addCars(List<CarDto> carDtos);

    Carrier responseCargo(Long cargoId, CarrierResponseDto carrierResponseDto);

    void deleteCar(Long carId);

    Carrier getCarrier();

    Carrier getCarrierById(Long id);

    void setScore(Carrier carrier, int stars);
}
