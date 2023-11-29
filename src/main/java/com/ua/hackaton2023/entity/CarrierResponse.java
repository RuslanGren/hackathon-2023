package com.ua.hackaton2023.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "responses")
public class CarrierResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private double cost;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;
}
