package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.carrier.CarDto;

import java.util.List;

public interface CarrierService {
    Carrier addCarrier(User user);

    Carrier addCar(CarDto carDto, Long id);

    Carrier getCarrierById(Long id);

    Carrier addCars(List<CarDto> carDtos, Long id);

    Carrier pickCargo(Long cargoId, Long carrierId);
}
