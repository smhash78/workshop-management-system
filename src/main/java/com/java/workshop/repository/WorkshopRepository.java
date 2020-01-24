package com.java.workshop.repository;

import com.java.workshop.model.workshop.Workshop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkshopRepository extends CrudRepository<Workshop, Long> {
}
