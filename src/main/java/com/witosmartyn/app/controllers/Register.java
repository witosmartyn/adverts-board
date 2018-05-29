package com.witosmartyn.app.controllers;

import com.witosmartyn.app.config.components.MyMessageSource;
import com.witosmartyn.app.config.constants.*;
import com.witosmartyn.app.config.handlers.MyRegisterHandler;
import com.witosmartyn.app.config.validators.UserDtoValidator;
import com.witosmartyn.app.entities.model.UserDto;
import com.witosmartyn.app.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Controller
public class Register {

    @Autowired
    private MyRegisterHandler registerHandler;

    @ModelAttribute
    public void addAttrib(Model model) {
        model.addAttribute(ATTR_NAME.PAGE_TITLE, PagesID.REGISTRATION);
    }

    @GetMapping("/register")
    public String showRegistration(Model model) {

        model.addAttribute("userDto", new UserDto());
        return VIEWS.REGISTER_FORM;
    }

    @PostMapping(value = "/register")
    public String register(@Valid UserDto userDto, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redAttributes) {
        return  registerHandler.registrationProcess(userDto, model, bindingResult, request,redAttributes);

    }

}
