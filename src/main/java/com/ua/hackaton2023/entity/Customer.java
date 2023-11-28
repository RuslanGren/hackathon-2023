package com.ua.hackaton2023.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "customer")
    private List<Cargo> activeCargos;

    @OneToMany(mappedBy = "customer")
    private List<Cargo> historyCargos;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Customer(User user) {
        this.user = user;
    }
}
