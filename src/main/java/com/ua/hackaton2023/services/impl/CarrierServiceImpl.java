package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.carrier.CarrierNotFoundException;
import com.ua.hackaton2023.repository.CarrierRepository;
import com.ua.hackaton2023.services.CarService;
import com.ua.hackaton2023.services.CargoService;
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
    private final CargoService cargoService;

    @Override
    public Carrier addCarrier(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return carrierRepository.save(new Carrier(user));
    }

    @Override
    public Carrier addCar(CarDto carDto, Long id) {
        Carrier carrier = getCarrierById(id);

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

    @Override
    public Carrier addCars(List<CarDto> carDtos, Long id) {
        Carrier carrier = getCarrierById(id);

        List<Car> carList = carService.createCars(carDtos, carrier);
        carService.saveCars(carList);

        List<Car> cars = carrier.getCars();
        cars.addAll(carList);
        carrier.setCars(cars);
        return carrierRepository.save(carrier);
    }

    @Override
    public Carrier pickCargo(Long cargoId, Long carrierId) {
        Carrier carrier = getCarrierById(carrierId);

        Cargo cargo = cargoService.getCargoById(cargoId);
        cargo.setCarrier(carrier);
        cargoService.saveCargo(cargo);

        List<Cargo> cargos = carrier.getCargosList();
        cargos.add(cargo);
        carrier.setCargosList(cargos);
        return carrierRepository.save(carrier);
    }
}
