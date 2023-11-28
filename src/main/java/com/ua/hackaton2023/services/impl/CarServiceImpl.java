package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.repository.CarRepository;
import com.ua.hackaton2023.services.CarService;
import com.ua.hackaton2023.web.carrier.CarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car createCar(CarDto carDto, Carrier carrier) {
        return Car.builder()
                .name(carDto.getName())
                .weight(carDto.getWeight())
                .volume(carDto.getVolume())
                .insurance(carDto.getInsurance())
                .carrier(carrier)
                .build();
    }

    @Override
    public void saveCar(Car car) {
        if (car == null) {
            throw new NullPointerException();
        }
        carRepository.save(car);
    }

    @Override
    public List<Car> createCars(List<CarDto> carDtos, Carrier carrier) {
        return carDtos.stream()
                .map(carDto -> createCar(carDto, carrier))
                .toList();
    }

    @Override
    public void saveCars(List<Car> cars) {
        if (cars == null) {
            throw new NullPointerException();
        }
        carRepository.saveAll(cars);
    }
}
