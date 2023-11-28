package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.web.cargo.CargoDto;

public interface CargoService {
    Cargo createCargo(CargoDto cargoDto, Customer customer);

    void saveCargo(Cargo cargo);

    Cargo getCargoById(Long id);
}
