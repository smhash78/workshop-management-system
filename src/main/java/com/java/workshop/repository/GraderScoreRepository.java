package com.java.workshop.repository;

import com.java.workshop.model.role.grader.GraderScore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraderScoreRepository extends CrudRepository<GraderScore, Long> {
}
