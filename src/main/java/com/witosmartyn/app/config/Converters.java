package com.witosmartyn.app.config;

import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.City;
import com.witosmartyn.app.services.CategoryService;
import com.witosmartyn.app.services.CityService;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * vitali
 */
@Configuration
@Log4j
public class Converters extends WebMvcConfigurerAdapter {
    private CityService cityService;
    private CategoryService categoryService;

    public Converters(CityService cityService, CategoryService categoryService) {
        this.cityService = cityService;
        this.categoryService = categoryService;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new StringToCategory());
        registry.addConverter(new StringToCity());
        registry.addConverter(new StringToItemField());
    }

    public class StringToCategory implements Converter<String, Category> {

        @Override
        public Category convert(String value) {
            if (value == null || value.equals("")) {
                return null;
            }
            if (isNumeric(value)) {
                return categoryService.findById(Long.valueOf(value));
            }
            return categoryService.findByName(value);
        }

    }

    public class StringToCity implements Converter<String, City> {
        private final String errorMsg = "Covert error ";

        @Override
        public City convert(String value) {
            if (value == null || value.equals("")) {
                log.debug(errorMsg +value+" t ocity");
                return null;

            }
            if (isNumeric(value)) {
                return cityService.findById(Long.valueOf(value));
            }
            return cityService.findByName(value);
        }

    }
    public class StringToItemField implements Converter<String, ItemField> {

        private final String errorMsg = "Value must be one of this : ";

        @Override
        public ItemField convert(String value) {
            if (value == null || value.equals("")) {
                log.warn(errorMsg +ItemField.valuesList());
                return ItemField.NAME;
            }
            try {
                return ItemField.valueOf(value.toUpperCase());
            } catch (Exception e) {
                log.warn(errorMsg +ItemField.valuesList());
                return ItemField.NAME;
            }

        }

    }




}
