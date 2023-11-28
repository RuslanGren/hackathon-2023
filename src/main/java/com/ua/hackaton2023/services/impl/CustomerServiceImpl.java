package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.customer.CustomerNotFoundException;
import com.ua.hackaton2023.repository.CustomerRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CargoService cargoService;

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public Customer addCargo(CargoDto cargoDto, Long id) {
        Customer customer = getCustomerById(id);

        Cargo cargo = cargoService.createCargo(cargoDto, customer);
        cargoService.saveCargo(cargo);

        List<Cargo> cargoList = customer.getCargoList();
        cargoList.add(cargo);
        customer.setCargoList(cargoList);
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer addCustomer(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return customerRepository.save(new Customer(user));
    }
}
