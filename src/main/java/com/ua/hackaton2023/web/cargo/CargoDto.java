package com.ua.hackaton2023.web.cargo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CargoDto {
    @NotBlank(message = "Імя не повинне бути пустим")
    @Size(max = 256, message = "Імя занадто довге")
    private String name;

    @NotBlank(message = "Опис не повинний бути пустим")
    @Size(max = 256, message = "Опис занадто довгий")
    private String description;

    @Min(value = 0, message = "Вага повинна бути більше нуля")
    @Max(value = 1_000_000, message = "Вага занадто велика")
    private double weight;

    @Min(value = 0, message = "Обєм повинний бути більше нуля")
    @Max(value = 1_000_000, message = "Обєм занадто великий")
    private double volume;

    @NotBlank(message = "Початкова адресса не може бути пустою")
    @Size(max = 256, message = "Початкова адресса занадто довга")
    private String startAddress;

    @NotBlank(message = "Кінцева адресса не може бути пустою")
    @Size(max = 256, message = "Кінцева адресса занадто довга")
    private String endAddress;

    private String insurance;
}
