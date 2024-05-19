package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.CarrierResponse;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.web.cargo.CargoDto;

import java.util.List;

public interface CargoService {
    Cargo createCargo(CargoDto cargoDto, Customer customer);

    Cargo saveCargo(Cargo cargo);

    Cargo getCargoById(Long id);

    void removeCargo(Cargo cargo);

    List<Cargo> getAllIsActive();
}
