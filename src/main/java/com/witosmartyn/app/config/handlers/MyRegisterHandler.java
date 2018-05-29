package com.witosmartyn.app.config.handlers;

/**
 * @author Vitalii Martyniuk
 * Handle process User registration
 */

import com.witosmartyn.app.config.components.MyMessageSource;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.Errors;
import com.witosmartyn.app.config.constants.Redirect;
import com.witosmartyn.app.config.constants.VIEWS;
import com.witosmartyn.app.config.validators.UserDtoValidator;
import com.witosmartyn.app.entities.model.UserDto;
import com.witosmartyn.app.services.RoleService;
import com.witosmartyn.app.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Component
public class MyRegisterHandler {
    @Value("${app.config.registration.auto-login-enabled:false}")
    private boolean autoLogonEnabled;

    @Value("${app.config.registration.auto-login-url:/}")
    private String redirectUrl;

    private UserService userService;
    private RoleService roleService;
    private UserDtoValidator userDtoValidator;
    private MyMessageSource msg;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    @Qualifier("myMessageSource")
    public void setMsg(MyMessageSource msg) {
        this.msg = msg;
    }

    @Autowired
    public void setUserDtoValidator(UserDtoValidator userDtoValidator) {
        this.userDtoValidator = userDtoValidator;
    } //setter injections


    public String registrationProcess(@Valid UserDto userDto,
                                      Model model,
                                      BindingResult bindingResult,
                                      HttpServletRequest request,
                                      RedirectAttributes  redAttributes) {
        //validation begin
        userDtoValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()
                || isUserExist(userDto, model)
                || !isValidConfirmPassword(userDto, model)
                ) {
            return VIEWS.REGISTER_FORM;
        } else {
            return createNewAccaunt(userDto, model, request,redAttributes);

        }
    }

    private String createNewAccaunt(UserDto userDto, Model model, HttpServletRequest request, RedirectAttributes  redAttributes) {
        userService.createAccaunt(userDto.getUsername(), userDto.getPassword(),
                new HashSet<>(Arrays.asList(roleService.getUserRole())));

        redAttributes.addFlashAttribute(ATTR_NAME.MSG_SUCCESS, msg.get("msg.register.successfull"));
        if (autoLogonEnabled) {

           return authWithHttpServletRequest(request, userDto.getUsername(), userDto.getPassword());
        }
        return Redirect.TO_LOGIN;
    }

    private boolean isValidConfirmPassword(UserDto userDto, Model model) {
        if (userDto.getConfirm().equals(userDto.getPassword())) {
            return true;
        }else{
            model.addAttribute(Errors.CONFIRM_PASSWORD_ERROR, true);
            log.info(Errors.CONFIRM_PASSWORD_ERROR);
            return false;

        }
    }

    private boolean isUserExist(UserDto userDto, Model model) {
        if (userService.isPresent(userDto.getUsername())) {
            model.addAttribute(Errors.USEREXIST, true);
            return true;
        }
        return false;
    }


    private String authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
            try {
                request.login(username, password);
            } catch (ServletException e) {
                log.error("Error while login ", e);
            }

        return "redirect:"+redirectUrl;
    }


}
