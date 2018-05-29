package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * vitali
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Transactional(readOnly = true)
    Role findByName(String name);

    @Override
    void delete(Role role);
}
