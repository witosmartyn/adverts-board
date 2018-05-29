package com.witosmartyn.app.config.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vitalii Martyniuk
 *
 * Enumeration that defined Type for populate database
 */
public enum Categories {
    ANIMALS,
    CHILD,
    CARS,
    CLOTHES,
    HOUSES,
    ELECTRONICS,
    EQUIPMENT,
    FOODS,
    SPORT,
    OTHERS;
    /**
     * @return @List available values
     */
    public static List<String> valuesList() {
        final ArrayList<String> names = new ArrayList<>();
        Arrays.asList(values()).forEach(city -> names.add(city.name()));
        return names;
    }

}
