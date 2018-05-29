package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.City;
import com.witosmartyn.app.repositories.CityRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

@Service()
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))

public class CityService {

    private CityRepository repo;


    public City save(City city) {
        return repo.save(city);
    }

    @Cacheable(value = "cities")
    public Collection<City> findAll() {
        log.warn("\n########################### ATTENTION #################################"+
                 "\n######### INVOKED METHOD findAll cities, should invoke cache !!! ######");
        return repo.findAll();
    }

    public City createIfNotFound(final String name) {

        City city = repo.findByName(name);
        if (city == null) {
            city = new City();
            city.setName(name);
        }
        return this.save(city);
    }
    public City findByName(String name) {
        return repo.findByName(name);
    }
    public City findById(Long id) {
        return repo.findOne(id);
    }
    public Long count() {
        return repo.count();
    }
}
