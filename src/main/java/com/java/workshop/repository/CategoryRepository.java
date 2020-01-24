package com.java.workshop.repository;

import com.java.workshop.model.workshop.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    public boolean existsByName(String name);
    public Category findByName(String name);
}
