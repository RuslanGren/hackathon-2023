package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {
    Customer getCustomerByUserDetails(UserDetails userDetails);

    Customer addCargo(CargoDto cargoDto, UserDetails userDetails);

    Customer addCustomer(User user, String name);

    void deleteCargo(Long cargoId, UserDetails userDetails);

    Cargo chooseCargoCarrier(Long cargoId, Long carrierResponseId, UserDetails userDetails);

    Cargo finishCargo(Long cargoId, int stars, UserDetails userDetails);
}
