package com.ua.hackaton2023.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "carriers")
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "carrier")
    private List<Cargo> historyCargos;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
