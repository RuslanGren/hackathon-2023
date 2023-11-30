package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.*;
import com.ua.hackaton2023.exceptions.cargo.CargoIsNotActiveException;
import com.ua.hackaton2023.exceptions.carrier.CarrierNotFoundException;
import com.ua.hackaton2023.repository.CarrierRepository;
import com.ua.hackaton2023.repository.CarrierResponseRepository;
import com.ua.hackaton2023.services.CarService;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;
    private final CarService carService;
    private final CargoService cargoService;
    private final CarrierResponseRepository carrierResponseRepository;

    @Override
    public Carrier addCarrier(User user, String name) {
        if (user == null) {
            throw new NullPointerException();
        }
        return carrierRepository.save(new Carrier(name, user));
    }

    @Override
    public Carrier addCar(CarDto carDto, UserDetails userDetails) {
        Carrier carrier = getCarrierByUserDetails(userDetails);

        Car car = carService.createCar(carDto, carrier);
        carService.saveCar(car);

        List<Car> cars = carrier.getCars();
        cars.add(car);
        carrier.setCars(cars);
        return carrierRepository.save(carrier);
    }

    @Override
    public Carrier getCarrierByUserDetails(UserDetails userDetails) {
        return carrierRepository.findByUser((User) userDetails);
    }

    @Override
    public Carrier getCarrierById(Long id) {
        return carrierRepository.findById(id).orElseThrow(CarrierNotFoundException::new);
    }

    @Override
    public Carrier addCars(List<CarDto> carDtos, UserDetails userDetails) {
        Carrier carrier = getCarrierByUserDetails(userDetails);

        List<Car> carList = carService.createCars(carDtos, carrier);
        carService.saveCars(carList);

        List<Car> cars = carrier.getCars();
        cars.addAll(carList);
        carrier.setCars(cars);
        return carrierRepository.save(carrier);
    }

    @Override
    public Carrier responseCargo(Long cargoId, CarrierResponseDto carrierResponseDto, UserDetails userDetails) {
        Cargo cargo = cargoService.getCargoById(cargoId);
        if (!cargo.isActive()) {
            throw new CargoIsNotActiveException();
        }
        Carrier carrier = getCarrierByUserDetails(userDetails);

        CarrierResponse carrierResponse = CarrierResponse.builder()
                .description(carrierResponseDto.getDescription())
                .cost(carrierResponseDto.getCost())
                .carrier(carrier)
                .cargo(cargo)
                .isApplied(false)
                .build();
        carrierResponseRepository.save(carrierResponse);

        List<CarrierResponse> responses = cargo.getResponses();
        responses.add(carrierResponse);
        cargo.setResponses(responses);
        cargoService.saveCargo(cargo);

        List<CarrierResponse> carrierResponses = carrier.getResponses();
        carrierResponses.add(carrierResponse);
        carrier.setResponses(carrierResponses);
        return carrierRepository.save(carrier);
    }

    @Override
    public void deleteCar(Long carId, UserDetails userDetails) {
        Carrier carrier = getCarrierByUserDetails(userDetails);
        Car car = carService.getCarById(carId);
        List<Car> cars = carrier.getCars();
        cars.remove(car);
        carrier.setCars(cars);

        carService.deleteCar(car);
        carrierRepository.save(carrier);
    }
}
