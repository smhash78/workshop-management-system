package com.java.workshop.repository;

import com.java.workshop.model.workshop.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
}
