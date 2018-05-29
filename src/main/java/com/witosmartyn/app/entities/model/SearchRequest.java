package com.witosmartyn.app.entities.model;

import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.City;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 *
 * vitali
 */
@Builder
@Data
public class SearchRequest {
    String query;
    Double fromPrice;
    Double toPrice;
    Integer page;
    Integer countOnPage;
    ItemField sortBy;
    Sort.Direction orderBy;
    City city;
    Category category;
    boolean byExample;
    boolean searchInDescription;
    boolean ignoreCase;
    PageRequest pageRequest;
}
