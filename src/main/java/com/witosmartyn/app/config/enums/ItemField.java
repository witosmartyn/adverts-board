package com.witosmartyn.app.config.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vitalii Martyniuk
 *
 * Enumeration that defined  sort representation @see {@link com.witosmartyn.app.entities.Item}
 * Field names that can be used for
 * <li>{@link #NAME}</li>
 * <li>{@link #CREATED}</li>
 * <li>{@link #UPDATED}</li>
 * <li>{@link #PRICE}</li>
 * <li>{@link #CATEGORY}</li>
 * <li>{@link #CITY}</li>
 */
public enum ItemField {

    NAME("name"),
    CREATED("created"),
    UPDATED("updated"),
    PRICE("price"),
    CATEGORY("category"),
    CITY("city");

    private final String text;

    /**
     * Constructor specifying enumeration by @param text
     */
   ItemField(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * @return lowercase represented value
     */
    public String val() {
        return name().toLowerCase();
    }

    /**
     * @return Default Instance
     */
    public ItemField getDefault() {
        return NAME;
    }

    /**
     * @return @List available values
     */
    public static List<String> valuesList() {
        final ArrayList<String> names = new ArrayList<>();
        Arrays.asList(values()).forEach(city -> names.add(city.name()));
        return names;
    }
}
