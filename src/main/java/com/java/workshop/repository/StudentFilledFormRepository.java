package com.java.workshop.repository;

import com.java.workshop.model.workshop.form.StudentFilledForm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFilledFormRepository extends CrudRepository<StudentFilledForm, Long> {
}
