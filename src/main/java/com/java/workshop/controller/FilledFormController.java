package com.java.workshop.controller;


import com.java.workshop.exception.MyAccessException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.workshop.Group;
import com.java.workshop.model.workshop.WorkshopSession;
import com.java.workshop.model.workshop.form.GroupAnswer;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import com.java.workshop.model.workshop.form.StudentFilledForm;
import com.java.workshop.repository.GroupFilledFormRepository;
import com.java.workshop.repository.StudentFilledFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/filled_forms")
public class FilledFormController {

    @Autowired
    private GroupFilledFormRepository groupFilledFormRepository;

    @Autowired
    private StudentFilledFormRepository studentFilledFormRepository;

    @GetMapping("/group/all")
    public ArrayList<GroupFilledForm> allGroupFilledForms() {
        return (ArrayList<GroupFilledForm>) groupFilledFormRepository.findAll();
    }

    @GetMapping("/student/all")
    public ArrayList<StudentFilledForm> allStudentFilledForms() {
        return (ArrayList<StudentFilledForm>) studentFilledFormRepository.findAll();
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<GroupFilledForm> groupFilledForm(@PathVariable Long id) {
        if (!groupFilledFormRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupFilledFormRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentFilledForm> studentFilledForm(@PathVariable Long id) {
        if (!studentFilledFormRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(studentFilledFormRepository.findById(id).get(), HttpStatus.OK);
    }
}
