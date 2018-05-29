package com.witosmartyn.app.config.validators;

/**
 * vitali
 */

import com.witosmartyn.app.entities.model.UserDto;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@Log4j
public class UserDtoValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        UserDto user = (UserDto) obj;

        String email = user.getUsername();

        if (!isValidEmail(email)) {
            errors.rejectValue("username", "msg.user.email.error");
        }

    }


    private boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
