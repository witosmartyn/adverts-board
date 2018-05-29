package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Image;
import com.witosmartyn.app.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * vitali
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional(readOnly = true)
    List<Image> findByItem(Item item);

    @Transactional(readOnly = true)
    Image findByName(String name);
}
