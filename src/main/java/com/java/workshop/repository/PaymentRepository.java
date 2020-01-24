package com.java.workshop.repository;

import com.java.workshop.model.role.Student.Payment;
import com.java.workshop.model.workshop.form.Form;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
