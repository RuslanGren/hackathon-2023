package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.CarrierResponse;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CustomerService {
    Customer getCustomer();
    Customer addCargo(CargoDto cargoDto);

    Customer addCustomer(User user, String name);

    void deleteCargo(Long cargoId);

    Cargo chooseCargoCarrier(Long cargoId, Long carrierResponseId);

    Cargo finishCargo(Long cargoId, int stars);

    List<Cargo> getCargosByUser(Long id);

    List<CarrierResponse> getAllCarrierResponsesByCustomerCargos();
}
