package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * vitali
 */

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Transactional(readOnly = true)
    Permission findByName(String name);

    @Override
    void delete(Permission permission);
}
