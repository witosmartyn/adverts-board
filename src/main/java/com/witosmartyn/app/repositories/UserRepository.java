package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * vitali
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Transactional(readOnly = true)
    User findUserByUsername(String username);
}
