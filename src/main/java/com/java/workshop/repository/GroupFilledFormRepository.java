package com.java.workshop.repository;

import com.java.workshop.model.workshop.form.Form;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupFilledFormRepository extends CrudRepository<GroupFilledForm, Long> {
}
