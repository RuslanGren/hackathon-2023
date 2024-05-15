package com.ua.hackaton2023.web.carrier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarrierResponseDto {
    private String description;
    private double cost;
}
