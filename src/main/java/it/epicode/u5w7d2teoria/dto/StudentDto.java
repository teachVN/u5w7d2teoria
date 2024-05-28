package it.epicode.u5w7d2teoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    @NotBlank(message = "Name can't be empty or null or only space")
    private String name;
    @NotBlank(message = "Surname can't be empty or null or only space")
    private String surname;
    @NotNull(message = "BirthDate can't be null")
    private LocalDate birthDate;
}
