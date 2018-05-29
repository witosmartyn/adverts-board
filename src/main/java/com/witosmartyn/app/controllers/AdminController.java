package com.witosmartyn.app.controllers;

import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.PagesID;
import com.witosmartyn.app.config.constants.Redirect;
import com.witosmartyn.app.config.constants.VIEWS;
import com.witosmartyn.app.entities.Role;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.services.ItemService;
import com.witosmartyn.app.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;

/**
 * User: vitali
 */
@Controller
@RequestMapping(path = "/control")
@Log4j
public class AdminController {
    @ModelAttribute
    public void addAttrib(Model model,HttpSession session) {
        model.addAttribute(ATTR_NAME.PAGE_TITLE,PagesID.ADMIN_PANEL);
        if (session.getAttribute(ATTR_NAME.USER_ITEMS_COUNT) == null) {
            Long userItemsCount = itemService.countUseritems(userService.principalUser());
            session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT, userItemsCount);
        }
    }

    @Autowired
    public AdminController(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    private UserService userService;
    private ItemService itemService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private Collection<Role> availableRoles;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute(ATTR_NAME.AVAILABLE_ROLES, availableRoles);
    }
//CONTROL_PAGE
    @GetMapping(value = {"/",""})
    public String showAdminPage(Model model) {
        model.addAttribute(ATTR_NAME.USERS, userService.findAll());
        return VIEWS.CONTROL_PAGE;
    }



    @PostMapping("/users/deleteall")
    public String deleteAll() {
        userService.deleteAll();
        return Redirect.TO_CONTROL;
    }

//EDIT
    @GetMapping(value = "/users/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model, HttpSession session) {
        final User user = userService.findById(id);
        user.setPassword("");//clear password for view
        session.setAttribute(ATTR_NAME.USER,user);
        model.addAttribute(ATTR_NAME.USER, user);
        return VIEWS.EDIT_USER_PAGE;
    }

    //ADD
    @GetMapping(value = "/users/add")
    public String showAdd(Model model) {
        final User user = userService.enhance(new User());
        model.addAttribute(ATTR_NAME.USER, user);
        return VIEWS.ADD_USER_PAGE;
    }
//ADD
    @PostMapping("/users/add")
    public String add(@Valid User newUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTR_NAME.USER, newUser);
            return VIEWS.ADD_USER_PAGE;
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.save(newUser);
        return Redirect.TO_CONTROL;
    }

        //    Update_user
    @PostMapping("/users/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model, HttpSession session) {
        bindingResult.rejectValue("password","NotEmpty");
        bindingResult.rejectValue("password","Size");
        log.debug("updateUser");
        if (bindingResult.hasFieldErrors("authorities")) {
            model.addAttribute(ATTR_NAME.USER, user);
            return VIEWS.EDIT_USER_PAGE;
        }

        final User oldUser = (User)session.getAttribute(ATTR_NAME.USER);
        if (user.getPassword().isEmpty()){//if  not changed
            oldUser.setPassword(userService.findById(user.getId()).getPassword());
        }else {//if changed
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        oldUser.setUsername(user.getUsername());
        oldUser.setAuthorities(user.getAuthorities());
        oldUser.setEnabled(user.isEnabled());
        oldUser.setAccountNonExpired(user.isAccountNonExpired());
        oldUser.setAccountNonLocked(user.isAccountNonLocked());
        oldUser.setCredentialsNonExpired(user.isCredentialsNonExpired());

        userService.save(oldUser);
        session.removeAttribute(ATTR_NAME.USER);
        return Redirect.TO_CONTROL;
    }

    //DELETE
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.delete(id);
        return Redirect.TO_CONTROL;
    }
}
