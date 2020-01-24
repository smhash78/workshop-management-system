package com.java.workshop.repository;

import com.java.workshop.model.workshop.form.GroupAnswer;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupAnswerRepository extends CrudRepository<GroupAnswer, Long> {
}
