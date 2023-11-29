package com.ua.hackaton2023.repository;

import com.ua.hackaton2023.entity.CarrierResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierResponseRepository extends JpaRepository<CarrierResponse, Long> {
}
