package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * vitali
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Transactional(readOnly = true)
    City findByName(String name);

}

