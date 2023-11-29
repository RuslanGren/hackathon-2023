package com.ua.hackaton2023.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cargos")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double weight;
    private double volume;
    private String insurance;
    @Column(name = "start_address")
    private String startAddress;
    @Column(name = "end_address")
    private String endAddress;
    @Column(name = "is_active")
    private boolean isActive;
    private boolean isFinished;
    private LocalDate date;

    @OneToMany(mappedBy = "cargo")
    private List<CarrierResponse> responses;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    @JsonBackReference
    private Carrier carrier;
}
