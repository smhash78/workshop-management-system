package com.java.workshop.controller;


import com.java.workshop.model.User;
import com.java.workshop.model.workshop.Category;
import com.java.workshop.model.workshop.WWRelation;
import com.java.workshop.model.workshop.Workshop;
import com.java.workshop.model.workshop.WorkshopWorkshopRelation;
import com.java.workshop.repository.CategoryRepository;
import com.java.workshop.repository.WWRelationRepository;
import com.java.workshop.repository.WorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/workshops")
public class WorkshopController {

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private WWRelationRepository wWRelationRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/all")
    public ArrayList<Workshop> allWorkshops() {
        return (ArrayList<Workshop>) workshopRepository.findAll();
    }

    @PostMapping("/create")
    // Prerequisites' ids are in params.
    // The workshop itself is in the request body.
    // More than name, the workshop can also have category.
    public ResponseEntity<Workshop> createWorkshop(@RequestBody Workshop newWorkshop,
                                                   @RequestParam String category,
                                                   @RequestParam List<Long> wids,
                                                   Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        for (Long wkid : wids) {
            if (!workshopRepository.existsById(wkid)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }

        if (!categoryRepository.existsByName(category)) {
            Category cat = new Category();
            cat.setName(category);
            categoryRepository.save(cat);
        }
        newWorkshop.setCategory(categoryRepository.findByName(category));
        ArrayList<Workshop> workshops = (ArrayList<Workshop>) workshopRepository.findAllById(wids);

        Workshop result = workshopRepository.save(newWorkshop);
        for (Workshop wk : workshops) {
            WorkshopWorkshopRelation relation = new WorkshopWorkshopRelation();
            relation.setRelationType(WWRelation.prerequisites);
            Optional<Workshop> retrievedWorkshop = workshopRepository.findById(wk.getId());
            relation.setSecondWorkshop(retrievedWorkshop.get());
            relation.setFirstWorkshop(newWorkshop);
            //
            wWRelationRepository.save(relation);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }
}
