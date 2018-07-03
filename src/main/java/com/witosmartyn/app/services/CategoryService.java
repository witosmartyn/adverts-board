package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.repositories.CategoryRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * vitali
 */
@Service()
@Slf4j
public class CategoryService {

    @Setter(onMethod= @__({@Autowired}))
    private  CategoryRepository repo;

    @PostConstruct
    public void init() {
       initCache();

    }

//  ToDo temporary solution
    private List<Category> myCache = new ArrayList<>();

    @Cacheable(value = "cate")
    public Collection<Category> findAll() {

//   ToDo   Temporary solution for a category myCache inactive !!!
        if (myCache.isEmpty()) {
                initCache();
            return myCache;
        }
        if (!myCache.isEmpty()) {
            log.debug("acces to custom category myCache");
            return myCache;
        }
        log.warn("\n ATTENTION ! INVOKED METHOD findAll categories , should invoke myCache !!! ");
        return repo.findAll();
    }

    private void initCache() {
        log.debug("initialize custom category myCache");
        this.myCache = repo.findAll();
    }

    public Category saveAndFlush(Category category) {
        evictCache();
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

    public void evictCache() {
        this.myCache.clear();
    }

}
