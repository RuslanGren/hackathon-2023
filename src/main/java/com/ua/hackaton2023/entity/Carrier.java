package com.ua.hackaton2023.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "carriers")
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private String description;
    private String address;

    @Column(name = "total_ratings")
    private int totalRatings = 0;
    @Column(name = "total_score")
    private int totalScore = 0;
    @Column(name = "average_score")
    private double averageScore;

    @OneToMany(mappedBy = "carrier", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Car> cars;

    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference
    private List<CarrierResponse> responses;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Carrier(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
