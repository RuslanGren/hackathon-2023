package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.web.carrier.CarDto;

import java.util.List;

public interface CarService {
    Car createCar(CarDto carDto, Carrier carrier);

    void saveCar(Car car);

    List<Car> createCars(List<CarDto> carDtos, Carrier carrier);

    void saveCars(List<Car> cars);

    void deleteCar(Car car);

    Car getCarById(Long id);
}
