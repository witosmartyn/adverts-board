package com.witosmartyn.app.services;

import com.witosmartyn.app.config.constants.Errors;
import com.witosmartyn.app.config.constants.ROLES;
import com.witosmartyn.app.entities.Role;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.*;


@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Log4j
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository users;
    private RoleService roleService;

    public User save(User user) {
        return  users.save(user);

    }


    //todo implement page request
    public List<User> findAll() {
        log.error("\n#################################################################################"+
                  "\n########################### ATTENTION ###########################################"+
                  "\n##### INVOKED METHOD findAll Users WITHOUT PAGE REQUEST Users from storage ######"+
                  "\n################################################################################");
        return users.findAll();
    }

    public void delete(Long id) {
        users.delete(id);
    }

    public void deleteAll() {
        users.deleteAll();
    }

    public long count() {
        return users.count();
    }

    public User createAccaunt(String username, String rowPassword, Set<Role> roles) {

        final User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rowPassword))
                .authorities(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        save(user);
        return user;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + Errors.NOT_FOUND));
    }

    public User findById(long id) {
        return users.findOne(id);

    }

    public Optional<User> findUserByUsername(String username) {
        return Optional
                .ofNullable(users.
                        findUserByUsername(username));
    }

    public boolean isPresent(String username) {
        return findUserByUsername(username).isPresent();
    }

    public User enhance(@NotNull User user) {
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            user.setAuthorities(new HashSet<>(Arrays.asList(roleService.getRole(ROLES.ROLE_USER))));
        }
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return user;
    }

    ////////////////// USER helper ////////////////////////////
    public User principalUser() {
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return currentUser;
    }
    public Optional<User> userOptional() {
        final Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication.isAuthenticated()){
            if (authentication.getPrincipal().equals("anonymousUser")){
            return Optional.empty();
            }
        }
        return Optional.of((User)authentication.getPrincipal());
    }
}
