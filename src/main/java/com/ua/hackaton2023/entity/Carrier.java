package com.ua.hackaton2023.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "carriers")
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference
    private List<Car> cars;

    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference
    private List<Cargo> cargosList;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Carrier(User user) {
        this.user = user;
    }
}
