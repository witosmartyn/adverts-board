package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.entities.model.CategoryAndCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * vitali
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional(readOnly = true)
    Item findByName(String name);

    @Transactional(readOnly = true)
    Optional<Item> findByid(Long id);

    List<Long> deleteItemsByUser(User user);

    @Transactional(readOnly = true)
    List<Item> findByUser(User user);

    @Transactional(readOnly = true)
    Long countItemByUser(User user);

    @Transactional(readOnly = true)
    Page<Item> findByUser(User user, Pageable pageRequest);
    @Transactional(readOnly = true)    Page<Item> findByNameContaining(String name, Pageable pageRequest);

    Page<Item> findByPriceBetween(Double fromPrice, Double toPrice, Pageable pageRequest);
    @Transactional(readOnly = true)
    Page<Item> findByNameContainingOrDescriptionContainsAllIgnoreCase(String name, String Description, Pageable pageRequest);

    //  SDJPA  Filtered by price and name(contains)
    @Transactional(readOnly = true)
    Page<Item> findByPriceBetweenAndNameContainsIgnoreCase(Double price0, Double price1, String name, Pageable pageRequest);


    @Transactional(readOnly = true)
    Long countItemByCategory(Category category);

}
