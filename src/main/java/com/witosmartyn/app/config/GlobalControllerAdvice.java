package com.witosmartyn.app.config;

import com.witosmartyn.app.config.components.MyMessageSource;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.controllers.Search;
import com.witosmartyn.app.services.CategoryService;
import com.witosmartyn.app.services.ItemService;
import com.witosmartyn.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

/**
 * vitali
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @ModelAttribute("somsing")
    public void populate(HttpSession session) {
        //System.out.println();
        }
}
