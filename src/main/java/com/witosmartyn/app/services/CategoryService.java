package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * vitali
 */
@Service()
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))

public class CategoryService {

    private  CategoryRepository repo;

    @Cacheable(value = "categories")
    public Collection<Category> findAll() {
        log.warn("\n########################### ATTENTION #################################"+
                "\n####### INVOKED METHOD findAll categories , should invoke cache !!! ####");
        return repo.findAll();
    }
    public Category saveAndFlush(Category category) {
        return repo.saveAndFlush(category);
    }

    public Category createIfNotFound(final String name ) {

        Category category = repo.findByName(name);
        if (category == null) {
            category = new Category();
            category.setName(name);
        }
        return this.saveAndFlush(category);
    }

    public Category findByName(String name) {
       return repo.findByName(name);
    }

    public Long count() {
        return repo.count();
    }

    public Category findById(Long id) {
        return repo.findOne(id);
    }
}
