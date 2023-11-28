package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.repository.CustomerRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CargoService cargoService;

    @Override
    public Customer getCustomerByUserDetails(UserDetails userDetails) {
        return customerRepository.findByUser((User) userDetails);
    }

    @Override
    public Customer addCargo(CargoDto cargoDto, UserDetails userDetails) {
        Customer customer = getCustomerByUserDetails(userDetails);

        Cargo cargo = cargoService.createCargo(cargoDto, customer);
        cargoService.saveCargo(cargo);

        List<Cargo> cargoList = customer.getCargoList();
        cargoList.add(cargo);
        customer.setCargoList(cargoList);
        return customerRepository.save(customer);
    }

    @Override
    public Customer addCustomer(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return customerRepository.save(new Customer(user));
    }
}
