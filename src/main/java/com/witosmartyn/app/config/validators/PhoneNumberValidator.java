package com.witosmartyn.app.config.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * vitali
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        boolean result;
        if (phoneField == null || phoneField.equals("")) {
            result = true;
        } else {
            result = phoneField.matches("[0-9]+")
                    && (phoneField.length() > 8
                    && phoneField.length() < 14);
        }
        return result;
    }
}
