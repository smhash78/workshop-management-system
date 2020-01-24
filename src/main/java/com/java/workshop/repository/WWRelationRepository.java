package com.java.workshop.repository;

import com.java.workshop.model.workshop.WorkshopWorkshopRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WWRelationRepository extends CrudRepository<WorkshopWorkshopRelation, Long> {
}
