package com.ua.hackaton2023.repository;

import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser(User user);
}
