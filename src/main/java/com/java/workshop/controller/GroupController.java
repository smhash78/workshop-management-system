package com.java.workshop.controller;


import com.java.workshop.exception.MyAccessException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.workshop.Group;
import com.java.workshop.model.workshop.WorkshopSession;
import com.java.workshop.model.workshop.form.GroupAnswer;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import com.java.workshop.repository.GroupRepository;
import com.java.workshop.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/all")
    public ArrayList<Group> allGroups() {
        return (ArrayList<Group>) groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> group(@PathVariable Long id) {
        if (!groupRepository.existsById(id)) {
            throw new MyNotFoundException("Group not found.");
        }
        return new ResponseEntity<>(groupRepository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("create/{sid}")
    public ResponseEntity<Group> create(@PathVariable Long sid) {
        Group group = new Group();
        group.setWorkshopSession(sessionRepository.findById(sid).get());
        group.setInternalId(sessionRepository.findById(sid).get().getGroups().size());
        return new ResponseEntity<>(groupRepository.save(group), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/filled_forms")
    public ArrayList<GroupFilledForm> groupFilledForms(@PathVariable Long id) {
        if (!groupRepository.existsById(id)) {
            throw new MyNotFoundException("Group not found.");
        }
        Group gp = groupRepository.findById(id).get();
        return (ArrayList<GroupFilledForm>) gp.getFilledForms();
    }
}
