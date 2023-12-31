package com.ua.hackaton2023.repository;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Carrier findByUser(User user);
}
