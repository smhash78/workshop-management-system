package com.java.workshop.repository;

import com.java.workshop.model.workshop.WorkshopSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<WorkshopSession, Long> {
}
