package com.java.workshop.controller;


import com.java.workshop.exception.MyConflictException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.role.grader.GraderResponsibility;
import com.java.workshop.model.role.grader.GraderScore;
import com.java.workshop.model.role.grader.GraderStatus;
import com.java.workshop.model.workshop.WorkshopSession;
import com.java.workshop.model.workshop.form.Form;
import com.java.workshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/graders")
public class GraderController {
    @Autowired
    private GraderRepository graderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GraderScoreRepository graderScoreRepository;

    @GetMapping("/all")
    public ArrayList<Grader> allGraders() {
        return (ArrayList<Grader>) graderRepository.findAll();
    }

    /*
    @PostMapping("/create/{id}") // user id
    public ResponseEntity<Grader> createGrader(@Valid @RequestBody Grader grd, @PathVariable Long id, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        grd.setUser(userRepository.findById(id).get());
        return new ResponseEntity<>(graderRepository.save(grd), HttpStatus.CREATED);
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<Grader> grader(@PathVariable Long id) {
        if (!graderRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(graderRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/session")
    public ResponseEntity<WorkshopSession> graderSession(@PathVariable Long id) {
        if (!graderRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Grader grader = graderRepository.findById(id).get();
        return new ResponseEntity<>(grader.getGroup().getWorkshopSession(), HttpStatus.OK);
    }


    @PostMapping("{grd_id}/change_grader_status/{new_status}")
    public ResponseEntity<Grader> changeGraderStatus(@PathVariable Long grd_id, @PathVariable GraderStatus new_status) {

        if (!graderRepository.existsById(grd_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Grader grd = graderRepository.findById(grd_id).get();
        grd.setStatus(new_status);
        return new ResponseEntity<>(graderRepository.save(grd), HttpStatus.OK);
    }

    @PostMapping("{grd_id}/change_grader_responsibility/{title}/{description}")
    public ResponseEntity<Grader> acceptRequest(@PathVariable Long grd_id,
                                                @PathVariable String title,
                                                @PathVariable String description) {

        if (!graderRepository.existsById(grd_id)) {
            throw new MyNotFoundException("Grader not found.");
        }
        Grader grd = graderRepository.findById(grd_id).get();
        if (grd.getStatus().equals(GraderStatus.REQUESTED)) {
            throw new MyConflictException("You can't change responsibility of requested grader. You should accept it first.");
        }
        GraderResponsibility grdres;
        if (grd.getResponsibility() == null) {
            grdres = new GraderResponsibility();
        }
        else {
            grdres = grd.getResponsibility();
        }
        grdres.setTitle(title);
        grdres.setDescription(description);
        grdres.setGrader(grd);
        responsibilityRepository.save(grdres);
        return new ResponseEntity<>(graderRepository.save(grd), HttpStatus.OK);
    }

    @PostMapping("{grd_id}/instructor_score")
    public ResponseEntity<GraderScore> graderScore(@PathVariable Long grd_id,
                                                @RequestBody GraderScore score) {

        if (!graderRepository.existsById(grd_id)) {
            throw new MyNotFoundException("Grader not found.");
        }

        Grader grader = graderRepository.findById(grd_id).get();
        score.setInsGrader(grader);
        graderRepository.save(grader);

        return new ResponseEntity<>(graderScoreRepository.save(score), HttpStatus.CREATED);
    }

    @PostMapping("{grd_id}/student_score")
    public ResponseEntity<GraderScore> studentScore(@PathVariable Long grd_id,
                                                     @RequestBody GraderScore score) {

        if (!graderRepository.existsById(grd_id)) {
            throw new MyNotFoundException("Grader not found.");
        }

        Grader grader = graderRepository.findById(grd_id).get();
        score.setGrader(grader);
        graderRepository.save(grader);

        return new ResponseEntity<>(graderScoreRepository.save(score), HttpStatus.CREATED);
    }

}
