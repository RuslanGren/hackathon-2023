package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.repository.CargoRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {
    private final CargoRepository cargoRepository;

    @Override
    public Cargo createCargo(CargoDto cargoDto, Customer customer) {
        return Cargo.builder()
                .name(cargoDto.getName())
                .description(cargoDto.getDescription())
                .weight(cargoDto.getWeight())
                .volume(cargoDto.getVolume())
                .startAddress(cargoDto.getStartAddress())
                .endAddress(cargoDto.getEndAddress())
                .customer(customer)
                .build();
    }

    @Override
    public void saveCargo(Cargo cargo) {
        if (cargo == null) {
            throw new NullPointerException();
        }
        cargoRepository.save(cargo);
    }
}
