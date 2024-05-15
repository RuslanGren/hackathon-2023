package com.ua.hackaton2023.web.carrier;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {
    @NotBlank(message = "Імя не повинно бути пустим")
    @Size(max = 255, message = "Імя занадто велике")
    private String name;

    @Min(value = 0, message = "Вантажопідйомність не може бути менше нуля")
    @Max(value = 1_000_000, message = "Помилка максимальної вантажопідйомності")
    private double weight;

    @Min(value = 0, message = "Обєм не може бути менше нуля")
    @Max(value = 1_000_000, message = "Помилка максимального обєму")
    private double volume;

    private String insurance;
}
