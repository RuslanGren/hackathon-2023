package com.ua.hackaton2023.web.carrier;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarrierResponseDto {
    private Long cargoId;
    private String description;
    private double cost;
}
