package com.java.workshop.repository;

import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.workshop.form.Form;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends CrudRepository<Form, Long> {
}
