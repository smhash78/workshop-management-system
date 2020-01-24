package com.java.workshop.repository;

import com.java.workshop.model.role.grader.GraderResponsibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsibilityRepository extends CrudRepository<GraderResponsibility, Long> {
}
