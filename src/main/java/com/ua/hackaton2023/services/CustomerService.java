package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.web.cargo.CargoDto;

import java.util.List;

public interface CustomerService {
    Customer getCustomerById(Long id);

    Customer addCargo(CargoDto cargoDto);

    List<Customer> getAll();

    Customer addCustomer(User user);
}
