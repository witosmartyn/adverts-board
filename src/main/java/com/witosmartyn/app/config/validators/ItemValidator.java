package com.witosmartyn.app.config.validators;

/**
 * vitali
 */

import com.witosmartyn.app.config.constants.ItemConfig;
import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.entities.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.StringUtils.isEmpty;


@Component
public class ItemValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
//
        Item item = (Item) obj;
        String name = item.getName();
        String description = item.getDescription();

        if (name == null
                || isEmpty(name)
                || name.length() < ItemConfig.MIN_NAME_LENGTH
                || name.length() > ItemConfig.MAX_NAME_LENGTH
                ) {
            Object[] parapsError = {ItemConfig.MIN_NAME_LENGTH, ItemConfig.MAX_NAME_LENGTH};
            errors.rejectValue("name", "msg.item.name.length",parapsError ,"error");

        }
        if (description == null
                || isEmpty(description)
                || description.length() < ItemConfig.MIN_DESCRIPTION_LENGTH
                || description.length() > ItemConfig.MAX_DESCRIPTION_LENGTH
                ) {
            Object[] paramsError = {ItemConfig.MIN_DESCRIPTION_LENGTH, ItemConfig.MAX_DESCRIPTION_LENGTH};
            errors.rejectValue("description", "msg.item.description.length",paramsError ,"error");

        }
        if (item.getCity() == null && ItemConfig.CITY_REQUIRED) {
            errors.rejectValue(ItemField.CITY.val(), "Msg.item.city.rule");
        }
        if (item.getCategory() == null && ItemConfig.CATEGORY_REQUIRED) {
            errors.rejectValue(ItemField.CATEGORY.val(), "Msg.item.category.required");
        }
        if (item.getPrice() == null && ItemConfig.PRICE_REQUIRED) {
            errors.rejectValue(ItemField.PRICE.val(), "Msg.item.price.required");
        }
        if (!isPhoneNumberValid(item.getPhone())) {
            errors.rejectValue("phone", "msg.item.phone.number.rule");
        }

    }

    public boolean isPhoneNumberValid(String phoneField) {
        boolean result;
        if (phoneField == null || phoneField.equals("") & !ItemConfig.PHONE_REQUIRED) {
            result = true;
        } else {
            result = phoneField.matches("[0-9]+")
                    && (phoneField.length() > ItemConfig.MIN_PHONE_LENGTH
                    && phoneField.length() < ItemConfig.MAX_PHONE_LENGTH);
        }
        return result;
    }
}
