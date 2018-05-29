package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.City;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.model.SearchRequest;
import com.witosmartyn.app.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * vitali
 */
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SearchService {
    private ItemRepository repository;


    public Page<Item> findByExample(SearchRequest sR) {
        return repository.findAll(this.getExample(sR), sR.getPageRequest());
    }

    public Example<Item> getExample(SearchRequest sR) {
        Example<Item> example = Example.of(Item.builder()
                .name(sR.getQuery())
                .description(sR.getQuery())
                .city(sR.getCity())
                .category(sR.getCategory())
                .build(), getExampleMatcher(sR.isIgnoreCase(), sR.getFromPrice(), sR.getToPrice()));
        return example ;
    }

    public ExampleMatcher getExampleMatcher(boolean ignoreCase,Double fromPrice, Double toPrice) {
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(ignoreCase);
    }


}
