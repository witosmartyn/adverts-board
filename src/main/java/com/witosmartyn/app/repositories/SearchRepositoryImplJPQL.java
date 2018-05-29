package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.model.SearchRequest;
import com.witosmartyn.app.repositories.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * vitali
 */
@Repository
public interface SearchRepositoryImplJPQL extends SearchRepository, JpaRepository<Item, Long> {

    /**
     * @return a Page of {@link Item}s.
     * @Author Vitali Martyniuk
     * Retrieve all {@link Item}s from the data store.
     * Return Filtered page list between min and max price
     * and Title or description contains queryString
     */

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(concat('%', :queryString,'%'))" +
            " OR LOWER(i.description) LIKE LOWER(concat('%', :description,'%')) ) AND i.price BETWEEN :min AND :max")
    Page<Item> findBySearchParams(@Param("min") Double min,
                                  @Param("max") Double max,
                                  @Param("queryString") String queryString,
                                  @Param("description") String description,
                                  @Param("pageable") Pageable pageable);


    /**
     * @return a Page of {@link Item}s.
     * @Author Vitali Martyniuk
     * Retrieve all {@link Item}s from the data store.
     * Return Filtered page list between min and max price
     * and Title Only
     */
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(concat('%', :queryString,'%')) AND i.price BETWEEN :min AND :max")
    Page<Item> byBetweenPriceAndNameIgnoreCase(@Param("min") Double min,
                                               @Param("max") Double max,
                                               @Param("queryString") String queryString,
                                               @Param("pageable") Pageable pageable); //title and price



    @Override
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(concat('%', :queryString,'%')) AND i.price BETWEEN :min AND :max")
    Page<Item> searchByRequest(SearchRequest sr, Pageable pageable);
}
