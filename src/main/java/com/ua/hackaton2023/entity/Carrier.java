package com.ua.hackaton2023.entity;

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

    @OneToMany(mappedBy = "carrier")
    private List<Cargo> cargosList;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Carrier(User user) {
        this.user = user;
    }
}
