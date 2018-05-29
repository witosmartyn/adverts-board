package com.witosmartyn.app.services;

import com.witosmartyn.app.config.constants.PERMISSIONS;
import com.witosmartyn.app.config.constants.ROLES;
import com.witosmartyn.app.config.enums.Categories;
import com.witosmartyn.app.config.enums.Cities;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.Permission;
import com.witosmartyn.app.entities.Role;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.repositories.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: vitali
 */
@Component
@Log4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))

public class DbPopulator {
    private final Set<Role> ADMIN_ROLES = new HashSet<>();
    private final Set<Role> USER_ROLES = new HashSet<>();

    @Value("${app.config.admin.username:admin@mail.ru}")
    private final String ADMIN_NAME ;

    @Value("${app.config.admin.password:admin}")
    private final String PASSWORD;

    private CityService cityService;

    private RoleService roleService;
    private UserService userService;
    private final CategoryService categoryService;
    private ItemService itemService;
    private PermissionRepository permissions;




    @PostConstruct
    public void onApplicationEvent() {
        // == create initial permission
        final Permission READ = createPermissionIfNotFound(PERMISSIONS.READ_PERMISSION);
        final Permission WRITE = createPermissionIfNotFound(PERMISSIONS.WRITE_PERMISSION);
        final Permission EDIT = createPermissionIfNotFound(PERMISSIONS.EDIT_PERMISSION);
        final Permission CHANGE_PASSWORD_PERMISSION = createPermissionIfNotFound(PERMISSIONS.CHANGE_PASSWORD_PERMISSION);

        // == create initial permission list
        final List<Permission> ADMIN_PERMISSIONS = new ArrayList<>(Arrays.asList(READ, WRITE, EDIT, CHANGE_PASSWORD_PERMISSION));
        final List<Permission> USER_PERMISSIONS = new ArrayList<>(Arrays.asList(READ, CHANGE_PASSWORD_PERMISSION));

        // == create initial ROLES
        final Role ADMIN_ROLE = createRole(ROLES.ROLE_ADMIN, ADMIN_PERMISSIONS);
        final Role ACTUATOR_ROLE = createRole(ROLES.ROLE_ACTUATOR, ADMIN_PERMISSIONS);
        final Role USER_ROLE = createRole(ROLES.ROLE_USER, USER_PERMISSIONS);

        this.ADMIN_ROLES.addAll(Arrays.asList(ADMIN_ROLE, ACTUATOR_ROLE, USER_ROLE));

        this.USER_ROLES.add(USER_ROLE);


        createDefaultCategories();
        createDefaultCities();
        createAdmin();

    }

    private void createDefaultCategories() {
        final List<String> categoryNames = Categories.valuesList();
        categoryNames.forEach(categoryService::createIfNotFound);

    }

    private void createDefaultCities() {
        final List<String> cityNames = Cities.valuesList();
        cityNames.forEach(cityService::createIfNotFound);

    }

    private void createAdmin() {
        createUserIfNotFound( ADMIN_NAME, PASSWORD, ADMIN_ROLES);
        if (log.isDebugEnabled()) {
            log.debug("# admin password: "+PASSWORD);

        }
    }

    private final User createUserIfNotFound(final String email, final String rawPassword, final Set<Role> roles) {
        User user = userService.findUserByUsername(email).orElseGet(
                () -> userService.createAccaunt(email, rawPassword, roles));

        return user;
    }

    private final Item createItemIfNotFound(final String name) {
        Item item = itemService.findByName(name);
        if (item == null) {
            item = new Item();
            item.setName("Advert " + name);
            item.setDescription("Описание " + name);
            item.setPrice(Math.ceil(Math.random()*100));
            item.setCity(cityService.findById(getRandomId(cityService.count())));
            item.setCategory(categoryService.findById(getRandomId(categoryService.count())));

            item.setPhone("8093"+(int)Math.ceil(Math.random()*10_000_000));
        }
        return item;
    }


    private final Permission createPermissionIfNotFound(final String name) {
        Permission permission = permissions.findByName(name);
        if (permission == null) {
            permission = new Permission(name);
            permission = permissions.save(permission);
        }
        return permission;
    }

    private final Role createRole(final String name, final Collection<Permission> permissions) {
        return roleService.createIfNotFound(name, permissions);
    }


    private Long getRandomId(Long max) {
        if (max!= null) {
            final long min = 1;
            final long range = (max - min) + 1;
            final long randomId = (int) (Math.random() * range) + min;
            return randomId;
        }
        return null;
    }


}
