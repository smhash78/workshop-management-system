package com.java.workshop.controller;


import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.User;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.role.Student.*;
import com.java.workshop.model.workshop.Group;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import com.java.workshop.model.workshop.form.StudentFilledForm;
import com.java.workshop.repository.InstructorRepository;
import com.java.workshop.repository.PaymentRepository;
import com.java.workshop.repository.StudentRepository;
import com.java.workshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/all")
    public ArrayList<Student> allStudents() {
        return (ArrayList<Student>) studentRepository.findAll();
    }

    @PostMapping("/create/{uid}") // user id
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student std, @PathVariable Long uid, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        std.setUser(userRepository.findById(uid).get());
        return new ResponseEntity<>(studentRepository.save(std), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/payments")
    public List<Payment> studentPayments (@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student student = studentRepository.findById(id).get();
        return student.getPayments();
    }

    @PostMapping("/{id}/change_status/{status}")
    public ResponseEntity<Student> changeStatus (@PathVariable Long id, @PathVariable StudentStatus status) {
        if (!studentRepository.existsById(id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student student = studentRepository.findById(id).get();
        student.setStatus(status);
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.OK);
    }

    @PostMapping("/{id}/change_payment_status/{status}")
    public ResponseEntity<Student> changePaymentStatus (@PathVariable Long id, @PathVariable StudentPaymentStatus status) {
        if (!studentRepository.existsById(id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student student = studentRepository.findById(id).get();
        student.setPaymentStatus(status);
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.OK);
    }

    @PostMapping("/{id}/change_attendancy/{status}")
    public ResponseEntity<Student> ChangeAttendancy (@PathVariable Long id, @PathVariable StudentAttendancy status) {
        if (!studentRepository.existsById(id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student student = studentRepository.findById(id).get();
        student.setAttendancy(status);
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.OK);
    }

    @GetMapping("/{id}/filled_forms")
    public ArrayList<StudentFilledForm> studentFilledForms(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student std = studentRepository.findById(id).get();
        return (ArrayList<StudentFilledForm>) std.getFilledForms();
    }
}
