package com.java.workshop.controller;


import com.java.workshop.exception.MyConflictException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.User;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.role.Student.Payment;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.Student.StudentPaymentStatus;
import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.role.grader.GraderStatus;
import com.java.workshop.model.workshop.Group;
import com.java.workshop.model.workshop.SessionRegisterStatus;
import com.java.workshop.model.workshop.Workshop;
import com.java.workshop.model.workshop.WorkshopSession;
import com.java.workshop.repository.*;
import javassist.NotFoundException;wedd
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GraderRepository graderRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ArrayList<User> allUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User u = userRepository.findByEmail(user.getEmail());
        if (u != null) {
            if (u.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(u, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> user (@PathVariable Long id){
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/instructors")
    public List<Instructor> userInstructors (@PathVariable Long id){
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("User not found.");
        }
        User user = userRepository.findById(id).get();
        return user.getInstructor();
    }

    @GetMapping("/{id}/students")
    public List<Student> userStudents (@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("User not found.");
        }
        User user = userRepository.findById(id).get();
        return user.getStudent();
    }

    @GetMapping("/{id}/graders")
    public List<Grader> userGraders (@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("Session not found.");
        }
        User user = userRepository.findById(id).get();

        return user.getGrader();
    }

    //graders/sessions

    @PostMapping("/{id}/register_cache/{wks_id}")
    public ResponseEntity<Student> wkRegisterChache (@PathVariable Long id, @PathVariable Long wks_id) throws RuntimeException{
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("User not found.");
        }
        if (!sessionRepository.existsById(wks_id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(wks_id).get();
        if (session.getInstructor().getUser().getId() == id) {
            throw new MyConflictException("Session's instructor can't be also student.");
        }
        if (session.isGraderById(id)) {
            throw new MyConflictException("Session's grader can't be also student.");
        }
        if (session.isStudentById(id)) {
            throw new MyConflictException("You've registered before.");
        }
        if (session.getStudentStatus().equals(SessionRegisterStatus.FULL)) {
            throw new MyConflictException("Session is full.");
        }
        else if (session.getCapacity() != null && session.getAllStudents().size() == session.getCapacity()) {
            session.setStudentStatus(SessionRegisterStatus.FULL);
            sessionRepository.save(session);
            throw new MyConflictException("Session is full.");
        }
        if (session.getCachePrice() == null) {
            throw new MyConflictException("Session's price can't be payed by cache.");
        }
        if (!userRepository.findById(id).get().hasPassedWorkshops(session.getWorkshop().getAllPrerequisites())) {
            throw new MyConflictException("Prerequisites hasn't passed.");
        }
        if (!userRepository.findById(id).get().hasTimeConflict(session)) {
            throw new MyConflictException("Time conflict with this workshop.");
        }
        Student std = new Student();
        std.setUser(userRepository.findById(id).get());
        for (Group gp : sessionRepository.findById(wks_id).get().getGroups()) {
            if (gp.getInternalId() == 0) {
                std.setGroup(gp);
            }
        }
        if (std.getGroup() == null) {
            throw new MyConflictException("Invalid session.");
        }

        if (session.getCachePrice() == 0) {
            std.setPaymentStatus(StudentPaymentStatus.PAYED);
        }
        else {
            std.setPaymentStatus(StudentPaymentStatus.NOT_PAYED);
        }
        std = studentRepository.save(std);

        if (session.getCachePrice() != 0) {
            Payment payment = new Payment();
            payment.setPayed(false);
            payment.setStudent(std);
            payment.setValue(session.getCachePrice());
            paymentRepository.save(payment);
        }
        return new ResponseEntity<>(std, HttpStatus.OK);
    }

    @PostMapping("/{id}/register_installment/{wks_id}")
    public ResponseEntity<Student> wkRegisterInstallment (@PathVariable Long id, @PathVariable Long wks_id) throws RuntimeException{
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("User not found.");
        }
        if (!sessionRepository.existsById(wks_id)) {
            throw new MyNotFoundException("Session not found.");
        }
        WorkshopSession session = sessionRepository.findById(wks_id).get();
        if (session.getInstructor().getUser().getId() == id) {
            throw new MyConflictException("Session's instructor can't be also student.");
        }
        if (session.isGraderById(id)) {
            throw new MyConflictException("Session's grader can't be also student.");
        }
        if (session.isStudentById(id)) {
            throw new MyConflictException("You've registered before.");
        }
        if (session.getStudentStatus().equals(SessionRegisterStatus.FULL)) {
            throw new MyConflictException("Session is full.");
        }
        else if (session.getCapacity() != null && session.getAllStudents().size() == session.getCapacity()) {
            session.setStudentStatus(SessionRegisterStatus.FULL);
            sessionRepository.save(session);
            throw new MyConflictException("Session is full.");
        }
        if ((session.getInstallmentPrice()) == null || (session.getNumberOfInstallments() == null)) {
            throw new MyConflictException("Session's price can't be payed by installments.");
        }
        if (!userRepository.findById(id).get().hasPassedWorkshops(session.getWorkshop().getAllPrerequisites())) {
            throw new MyConflictException("Prerequisites hasn't passed.");
        }
        Student std = new Student();
        std.setUser(userRepository.findById(id).get());
        std.setGroup(sessionRepository.findById(wks_id).get().getGroups().get(0));
        std.setPaymentStatus(StudentPaymentStatus.NOT_PAYED);
        std = studentRepository.save(std);

        int i;
        for (i = 0; i < session.getNumberOfInstallments(); i++) {
            Payment payment = new Payment();
            payment.setPayed(false);
            payment.setStudent(std);
            payment.setValue(session.getInstallmentPrice()/session.getNumberOfInstallments());
            paymentRepository.save(payment);
        }
        return new ResponseEntity<>(std, HttpStatus.OK);
    }

    @PostMapping("/{id}/request/{wks_id}")
    public ResponseEntity<Grader> wkRequest (@PathVariable Long id, @PathVariable Long wks_id) throws RuntimeException{
        if (!userRepository.existsById(id)) {
            throw new MyNotFoundException("User not found.");
        }
        if (!sessionRepository.existsById(wks_id)) {
            throw new MyNotFoundException("Workshop session not found.");
        }
        WorkshopSession session = sessionRepository.findById(wks_id).get();
        if (session.getInstructor().getUser().getId() == id) {
            throw new MyConflictException("Session's instructor can't be also grader.");
        }
        if (session.isRequestedForGraderById(id)) {
            throw new MyConflictException("You have requested before.");
        }
        if (session.isStudentById(id)) {
            throw new MyConflictException("Session's student can't be also grader.");
        }
        if (session.getGraderStatus().equals(SessionRegisterStatus.FULL)) {
            throw new MyConflictException("This session doesn't accept grader requests anymore.");
        }
        if (!userRepository.findById(id).get().hasPassedWorkshops(session.getWorkshop().getAllPrerequisites())) {
            throw new MyConflictException("Prerequisites hasn't passed.");
        }
        Grader grd = new Grader();
        grd.setGroup(sessionRepository.findById(wks_id).get().getGroups().get(0));
        grd.setUser(userRepository.findById(id).get());
        grd.setStatus(GraderStatus.REQUESTED);

        return new ResponseEntity<>(graderRepository.save(grd), HttpStatus.OK);
    }
}
