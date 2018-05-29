package com.witosmartyn.app.services;

import com.witosmartyn.app.config.constants.ROLES;
import com.witosmartyn.app.entities.Permission;
import com.witosmartyn.app.entities.Role;
import com.witosmartyn.app.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: vitali
 */
@Service
@Log4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class RoleService {

    private RoleRepository roleRepository;
    //ToDo create cached map

    public  Role createIfNotFound(final String name, final Collection<Permission> permissions) {
        Role role = getRole(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPermissions(permissions);
        role = roleRepository.saveAndFlush(role);
        return role;
    }

    public  Role getRole(final String name) {
        return roleRepository.findByName(name);
    }

    public  Role getAdminRole() {
        return getRole(ROLES.ROLE_ADMIN);
    }
    public  Role getUserRole() {
        return getRole(ROLES.ROLE_USER);
    }
    public  Role getActuatorRole() {
        return getRole(ROLES.ROLE_ACTUATOR);
    }

    public Collection<Role> findAll() {
        log.warn("\n########################### ATTENTION #################################"+
                "\n####### INVOKED METHOD findAll Roles, should invoke cache !!! #########");
        return roleRepository.findAll();
    }
}
