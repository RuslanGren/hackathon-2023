package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.exceptions.cargo.CargoNotFoundException;
import com.ua.hackaton2023.repository.CargoRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {
    private final CargoRepository cargoRepository;

    @Override
    public List<Cargo> getAll() {
        return cargoRepository.findAll();
    }

    @Override
    public Cargo createCargo(CargoDto cargoDto, Customer customer) {
        return Cargo.builder()
                .name(cargoDto.getName())
                .description(cargoDto.getDescription())
                .weight(cargoDto.getWeight())
                .startAddress(cargoDto.getStartAddress())
                .endAddress(cargoDto.getEndAddress())
                .customer(customer)
                .isActive(true)
                .date(LocalDate.now())
                .build();
    }

    @Override
    public Cargo saveCargo(Cargo cargo) {
        if (cargo == null) {
            throw new NullPointerException();
        }
        return cargoRepository.save(cargo);
    }

    @Override
    public Cargo getCargoById(Long id) {
        return cargoRepository.findById(id).orElseThrow(CargoNotFoundException::new);
    }

    @Override
    public void removeCargo(Cargo cargo) {
        cargoRepository.delete(cargo);
    }
}
