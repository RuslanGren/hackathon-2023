package com.ua.hackaton2023.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Table(name = "responses")
public class CarrierResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private double cost;
    private boolean isApplied;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;
}
