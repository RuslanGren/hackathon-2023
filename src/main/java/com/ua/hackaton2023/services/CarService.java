package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.web.carrier.CarDto;

public interface CarService {
    Car createCar(CarDto carDto, Carrier carrier);

    void saveCar(Car car);
}
