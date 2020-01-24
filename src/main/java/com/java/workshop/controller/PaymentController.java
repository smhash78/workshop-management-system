package com.java.workshop.controller;

import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.role.Student.Payment;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.Student.StudentPaymentStatus;
import com.java.workshop.repository.PaymentRepository;
import com.java.workshop.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/all")
    public ArrayList<Payment> allPayments() {
        return (ArrayList<Payment>) paymentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> payment(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentRepository.findById(id).get(), HttpStatus.OK);
    }

/*
    @GetMapping("/create")
    public void create() {
        Payment p = new Payment();
        p.setPayed(false);
        p.setStudent(studentRepository.findById((long) 1).get());
        paymentRepository.save(p);
    }

 */

    @PostMapping("{id}/pay")
    public ResponseEntity<Payment> paymentPay(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Payment payment = paymentRepository.findById(id).get();
        payment.setPayed(true);
        payment.setPayedDate(new Date());

        Student std = payment.getStudent();
        if (std.isAllPayed()) {
            std.setPaymentStatus(StudentPaymentStatus.PAYED);
        }
        else {
            std.setPaymentStatus(StudentPaymentStatus.HALF_PAYED);
        }
        studentRepository.save(std);
        return new ResponseEntity<>(paymentRepository.save(payment), HttpStatus.OK);
    }
}
