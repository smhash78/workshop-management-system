package com.java.workshop.controller;


import com.java.workshop.model.place.Place;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.repository.InstructorRepository;
import com.java.workshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/instructors")
public class InstructorController {
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ArrayList<Instructor> allInstructors() {
        return (ArrayList<Instructor>) instructorRepository.findAll();
    }

    @PostMapping("/create/{uid}") // user id
    public ResponseEntity<Instructor> createInstructor(@Valid @RequestBody Instructor ins, @PathVariable Long uid, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        ins.setUser(userRepository.findById(uid).get());
        return new ResponseEntity<>(instructorRepository.save(ins), HttpStatus.CREATED);
    }
}
