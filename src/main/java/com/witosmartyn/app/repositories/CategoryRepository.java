package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * vitali
 */
@Repository(value = "categoryRepo")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Transactional(readOnly = true)
    Category findByName(String name);



}
