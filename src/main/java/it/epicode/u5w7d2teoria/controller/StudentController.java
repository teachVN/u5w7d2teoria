package it.epicode.u5w7d2teoria.controller;

import it.epicode.u5w7d2teoria.dto.StudentDto;
import it.epicode.u5w7d2teoria.entity.Student;
import it.epicode.u5w7d2teoria.exception.BadRequestException;
import it.epicode.u5w7d2teoria.exception.NotFoundException;
import it.epicode.u5w7d2teoria.service.StudentService;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostMapping("/api/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveStudent(@RequestBody @Validated StudentDto studentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(error->error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }

        return studentService.saveStudent(studentDto);
    }
    @GetMapping("/api/students")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }
    @GetMapping("/api/students/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Student getStudentById(@PathVariable int id){
        Optional<Student> studentOptional= studentService.getStudentById(id);

        if(studentOptional.isPresent()){
            return studentOptional.get();
        }
        else{
            throw new NotFoundException("Student with id=" + id + " not found");
        }
    }
    @PutMapping("/api/students/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Student updateStudent(@PathVariable int id, @RequestBody @Validated StudentDto studentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(error->error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }

        return studentService.updateStudent(id, studentDto);
    }
    @DeleteMapping("/api/students/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStudent(@PathVariable int id){
        return studentService.deleteStudent(id);
    }
}
