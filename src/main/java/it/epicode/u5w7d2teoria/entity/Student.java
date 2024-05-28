package it.epicode.u5w7d2teoria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String surname;
    private LocalDate birthDate;

}
