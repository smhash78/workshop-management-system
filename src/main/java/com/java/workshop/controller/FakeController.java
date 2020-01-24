package com.java.workshop.controller;


import com.java.workshop.model.User;
import com.java.workshop.model.place.City;
import com.java.workshop.model.place.Place;
import com.java.workshop.model.place.State;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.workshop.*;
import com.java.workshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

// ye chize alaki inja

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1")
public class FakeController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkshopRepository wkRepository;

    @Autowired
    private WWRelationRepository wWRelationRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @PutMapping("/fake")
    public void fake() {
        ArrayList<String> names = new ArrayList<>();
        names.add("mamad@yahoo.com");
        names.add("reza@yahoo.com");
        names.add("ali@yahoo.com");
        names.add("ostad@yahoo.com");
        names.add("ostad2@yahoo.com");
        names.add("ostad3@yahoo.com");

        ArrayList<String> fnames = new ArrayList<>(Arrays.asList("Mammad", "Reza", "Ali", "Ahmad", "Sadr", "Hamze"));

        ArrayList<String> lnames = new ArrayList<>(Arrays.asList("Mammadian", "Rezaee", "Alizade", "Tohidi", "Sadri", "Hamzeee"));

        int i = 0;
        for (String name : names) {
            User user = new User();
            user.setEmail(name);
            user.setFirstName(fnames.get(i));
            user.setLastName(lnames.get(i));
            user.setPassword("hello123");
            user.setBirthDate(new Date());
            i++;
            if (name.equals("ostad@yahoo.com") || name.equals("ostad2@yahoo.com") || name.equals("ostad3@yahoo.com")) {
                userRepository.save(user);
                Instructor instructor = new Instructor();
                instructor.setUser(user);
                instructorRepository.save(instructor);
                continue;
            }
            userRepository.save(user);
        }



        ArrayList<State> snames = new ArrayList<>(Arrays.asList(State.FARS, State.TEHRAN));
        ArrayList<City> cnames = new ArrayList<>(Arrays.asList(City.FARS_SHIRAZ, City.TEHRAN_TEHRAN));
        for (i = 0; i < 2; i++) {
            Place plc = new Place();
            plc.setState(snames.get(i));
            plc.setCity(cnames.get(i));
            plc.setAddress("Khiaban - Kooche" + i);
            placeRepository.save(plc);
        }
        ArrayList<String> catnames = new ArrayList<>(Arrays.asList("Programming", "Bargh"));
        Category cat = new Category();
        cat.setName(catnames.get(0));
        categoryRepository.save(cat);
        Category cat2 = new Category();
        cat2.setName(catnames.get(1));
        categoryRepository.save(cat2);
        ArrayList<String> wnames = new ArrayList<>(Arrays.asList("Basic Programming", "Advanced Programming", "Circuit Theory"));
        for (String wname : wnames) {
            Workshop wk = new Workshop();
            wk.setName(wname);
            if (wname.equals("Circuit Theory")) {
                wk.setCategory(categoryRepository.findByName("Bargh"));
            }
            else {
                wk.setCategory(categoryRepository.findByName("Programming"));
            }
            if (wname.equals("Advanced Programming")) {
                wkRepository.save(wk);
                WorkshopWorkshopRelation relation = new WorkshopWorkshopRelation();
                relation.setRelationType(WWRelation.prerequisites);
                Optional<Workshop> retrievedWorkshop = wkRepository.findById((long) 1);
                relation.setSecondWorkshop(retrievedWorkshop.get());
                relation.setFirstWorkshop(wk);
                wWRelationRepository.save(relation);
                continue;
            }
            wkRepository.save(wk);
        }

        WorkshopSession wks = new WorkshopSession();
        wks.setName("Tohidi Basic Programming");
        wks.setPlace(placeRepository.findById((long) 1).get());
        wks.setWorkshop(wkRepository.findById((long) 1).get());
        wks.setGraderStatus(SessionRegisterStatus.NEED);
        wks.setStudentStatus(SessionRegisterStatus.NEED);
        wks.setCachePrice(0);
        Instructor ins = instructorRepository.findById((long) 1).get();
        ins.setSession(wks);

        sessionRepository.save(wks);

        WorkshopSession wks2 = new WorkshopSession();
        wks2.setName("Sadr Basic Programming");
        wks2.setPlace(placeRepository.findById((long) 2).get());
        wks2.setWorkshop(wkRepository.findById((long) 1).get());
        wks2.setGraderStatus(SessionRegisterStatus.NEED);
        wks2.setStudentStatus(SessionRegisterStatus.NEED);
        wks2.setCachePrice(100);
        Instructor ins2 = instructorRepository.findById((long) 2).get();
        ins2.setSession(wks2);

        sessionRepository.save(wks2);

        WorkshopSession wks3 = new WorkshopSession();
        wks3.setName("Hamze Advanced Programming");
        wks3.setPlace(placeRepository.findById((long) 2).get());
        wks3.setWorkshop(wkRepository.findById((long) 2).get());
        wks3.setGraderStatus(SessionRegisterStatus.NEED);
        wks3.setStudentStatus(SessionRegisterStatus.NEED);
        wks3.setCachePrice(0);
        Instructor ins3 = instructorRepository.findById((long) 3).get();
        ins3.setSession(wks3);

        sessionRepository.save(wks3);

        Group group = new Group();
        group.setWorkshopSession(wks);
        group.setInternalId(0);
        groupRepository.save(group);

        Group group2 = new Group();
        group2.setWorkshopSession(wks2);
        group2.setInternalId(0);
        groupRepository.save(group2);

        Group group3 = new Group();
        group3.setWorkshopSession(wks3);
        group3.setInternalId(0);
        groupRepository.save(group3);

        instructorRepository.save(ins);
    }


}
