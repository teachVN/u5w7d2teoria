package it.epicode.u5w7d2teoria.repository;

import it.epicode.u5w7d2teoria.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
