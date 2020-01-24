package com.java.workshop.controller;

import com.java.workshop.model.place.Place;
import com.java.workshop.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/places")
public class PlaceController {
    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping("/all")
    public ArrayList<Place> allPlaces() {
        return (ArrayList<Place>) placeRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Place> createPlace(@Valid @RequestBody Place place, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(placeRepository.save(place), HttpStatus.CREATED);
    }

}
