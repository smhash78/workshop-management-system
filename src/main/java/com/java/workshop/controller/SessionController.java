package com.java.workshop.controller;


import com.java.workshop.exception.MyConflictException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.Student.StudentPaymentStatus;
import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.role.grader.GraderStatus;
import com.java.workshop.model.workshop.*;
import com.java.workshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/sessions")
public class SessionController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GraderRepository graderRepository;

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private GroupFilledFormRepository groupFilledFormRepository;

    @Autowired
    private GroupAnswerRepository groupAnswerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SWRelationRepository sWRelationRepository;

    @GetMapping("/all")
    public ArrayList<WorkshopSession> allSessions() {
        return (ArrayList<WorkshopSession>) sessionRepository.findAll();
    }

    @GetMapping("/search/{type}/{key_words}")
    public ArrayList<WorkshopSession> searchByKeyWords(@PathVariable SessionType type, @PathVariable String key_words, @Nullable @RequestParam String category) {
        key_words = key_words.toLowerCase();
        String[] keyWords = key_words.split(" ");
        ArrayList<WorkshopSession> sessions = new ArrayList<>();
        if (category == null) { //search without category
            for (WorkshopSession ses : sessionRepository.findAll()) {
                if (type.equals(SessionType.ALL) || ses.hasType(type)) {
                    for (String keyWord : keyWords) {
                        if (ses.getName().toLowerCase().contains(keyWord)) {
                            sessions.add(ses);
                            break;
                        }
                    }
                }
            }
            return sessions;
        }
        //search with category
        if (!categoryRepository.existsByName(category)) {
            throw new MyNotFoundException("Category not found.");
        }
        for (WorkshopSession ses : sessionRepository.findAll()) {
            if ((type.equals(SessionType.ALL) || ses.hasType(type)) &&
                    ses.getWorkshop().getCategory().getName().equals(category)) {
                for (String keyWord : keyWords) {
                    if (ses.getName().toLowerCase().contains(keyWord)) {
                        sessions.add(ses);
                        break;
                    }
                }
            }
        }
        return sessions;
    }

    @GetMapping("/search/{type}")
    public ArrayList<WorkshopSession> search(@PathVariable SessionType type, @Nullable @RequestParam String category) {
        ArrayList<WorkshopSession> sessions = new ArrayList<>();
        if (category == null) { //search without category
            for (WorkshopSession ses : sessionRepository.findAll()) {
                if (type.equals(SessionType.ALL) || ses.hasType(type)) {
                    sessions.add(ses);
                }
            }
            return sessions;
        }
        // search with category
        if (!categoryRepository.existsByName(category)) {
            throw new MyNotFoundException("Category not found.");
        }
        for (WorkshopSession ses : sessionRepository.findAll()) {
            if ((type.equals(SessionType.ALL) || ses.hasType(type)) &&
                    ses.getWorkshop().getCategory().getName().equals(category)) {
                sessions.add(ses);
            }
        }
        return sessions;
    }

    @GetMapping("/category/{id}")
    public ArrayList<WorkshopSession> categorySessions(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new MyNotFoundException("Category not found.");
        }
        ArrayList<WorkshopSession> sessions = new ArrayList<>();
        List<Workshop> wrks = categoryRepository.findById(id).get().getWorkshops();
        for (Workshop wrk : wrks) {
            for (WorkshopSession wrkses : wrk.getSessions()) {
                sessions.add(wrkses);
            }
        }

        return sessions;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopSession> session(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sessionRepository.findById(id).get(), HttpStatus.OK);

    }

    // TODO Redundancy handle
    @PostMapping("/create/{place_id}/{wk_id}/{user_id}") // user is instructor
    public ResponseEntity<WorkshopSession> createSession(@RequestBody WorkshopSession wks,
                                                         @RequestParam List<Long> wkids,
                                                         @PathVariable Long place_id,
                                                         @PathVariable Long wk_id,
                                                         @PathVariable Long user_id,
                                                         Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (wks.getInstallmentPrice() != null) {
            if ((wks.getInstallmentPrice() == 0) || ((wks.getInstallmentPrice() != null) && wks.getNumberOfInstallments() == null)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        if (!placeRepository.existsById(place_id) ||
                !workshopRepository.existsById(wk_id) ||
                !userRepository.existsById(user_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for (Long wkid : wkids) {
            if (!workshopRepository.existsById(wkid)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }

        ArrayList<Workshop> workshops = (ArrayList<Workshop>) workshopRepository.findAllById(wkids);

        wks.setPlace(placeRepository.findById(place_id).get());
        wks.setWorkshop(workshopRepository.findById(wk_id).get());
        wks.setGraderStatus(SessionRegisterStatus.NEED);
        wks.setStudentStatus(SessionRegisterStatus.NEED);
        Group group = new Group();
        group.setWorkshopSession(wks);
        group.setInternalId(0);
        wks = sessionRepository.save(wks);
        Instructor ins = new Instructor();
        ins.setSession(wks);
        ins.setUser(userRepository.findById(user_id).get());
        instructorRepository.save(ins);
        wks = sessionRepository.findById(wks.getId()).get();
        wks.setInstructor(ins);
        wks = sessionRepository.save(wks);
        groupRepository.save(group);
        for (Workshop workshop : workshops) {
            SessionWorkshopRelation relation = new SessionWorkshopRelation();
            relation.setSession(wks);
            relation.setWorkshop(workshop);
            relation.setRelationType(WWRelation.prerequisites);
            sWRelationRepository.save(relation);
        }


        return new ResponseEntity<>(wks, HttpStatus.CREATED);
    }

    @GetMapping("{id}/groups")
    public List<Group> allGroups(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        return session.getGroups();
    }

    @GetMapping("{id}/graders")
    public ArrayList<Grader> allGraders(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        return session.getAllGraders();
    }

    @GetMapping("{id}/requested_graders")
    public ArrayList<Grader> requestedGraders(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        return session.getAllRequestedGraders();
    }

    @PostMapping("{id}/change_grader_group/{grd_id}/{gp_id}")
    public ResponseEntity<Grader> changeGraderGroup(@PathVariable Long id, @PathVariable Long grd_id, @PathVariable Long gp_id) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        if (!graderRepository.existsById(grd_id)) {
            throw new MyNotFoundException("Grader not found.");
        }
        Grader grd = graderRepository.findById(grd_id).get();
        if (grd.getGroup().getWorkshopSession().getId() != id) {
            throw new MyNotFoundException("Grader not found in the session.");
        }
        if (grd.getStatus().equals(GraderStatus.REQUESTED)) {
            throw new MyConflictException("You can't change group of requested grader. You should accept it first.");
        }
        grd.setGroup(groupRepository.findById(gp_id).get());
        return new ResponseEntity<>(graderRepository.save(grd), HttpStatus.OK);
    }

    @PostMapping("{id}/change_student_group/{std_id}/{gp_id}")
    public ResponseEntity<Student> changeStudentGroup(@PathVariable Long id, @PathVariable Long std_id, @PathVariable Long gp_id) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        if (!studentRepository.existsById(std_id)) {
            throw new MyNotFoundException("Student not found.");
        }
        Student std = studentRepository.findById(std_id).get();
        if (std.getGroup().getWorkshopSession().getId() != id) {
            throw new MyNotFoundException("Grader not found in the session.");
        }
        if (!std.getPaymentStatus().equals(StudentPaymentStatus.PAYED)) {
            throw new MyConflictException("You can't change group of not fully-paid student.");
        }
        std.setGroup(groupRepository.findById(gp_id).get());
        return new ResponseEntity<>(studentRepository.save(std), HttpStatus.OK);
    }

    @PostMapping("{id}/change_grader_status/{status}")
    public ResponseEntity<WorkshopSession> changeGraderStatus(@PathVariable Long id, @PathVariable SessionRegisterStatus status) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        session.setGraderStatus(status);
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.OK);
    }

    @GetMapping("{id}/payment_students/{status}")
    public ArrayList<Student> notPayedStudents(@PathVariable Long id, @PathVariable StudentPaymentStatus status) {
        if (!sessionRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        return session.returnAllStatusStudents(status);
    }

    @PostMapping("{id}/change_student_status/{status}")
    public ResponseEntity<WorkshopSession> changeStudentStatus(@PathVariable Long id, @PathVariable SessionRegisterStatus status) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        WorkshopSession session = sessionRepository.findById(id).get();
        session.setStudentStatus(status);
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.OK);
    }



}

