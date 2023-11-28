package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.carrier.CarrierNotFoundException;
import com.ua.hackaton2023.repository.CarrierRepository;
import com.ua.hackaton2023.services.CarService;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.web.carrier.CarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;
    private final CarService carService;

    @Override
    public Carrier addCarrier(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return carrierRepository.save(new Carrier(user));
    }

    @Override
    public Carrier addCar(CarDto carDto) {
        Carrier carrier = getCarrierById(carDto.getCarrierId());

        Car car = carService.createCar(carDto, carrier);
        carService.saveCar(car);

        List<Car> cars = carrier.getCars();
        cars.add(car);
        carrier.setCars(cars);
        return carrierRepository.save(carrier);
    }

    @Override
    public Carrier getCarrierById(Long id) {
        return carrierRepository.findById(id).orElseThrow(CarrierNotFoundException::new);
    }
}
