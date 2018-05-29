package com.witosmartyn.app.config.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * vitali
 */

public class ItemConfig {

    public static final int MIN_NAME_LENGTH = 3;

    public static final int MAX_NAME_LENGTH = 50;

    public static final int MIN_DESCRIPTION_LENGTH = 3;

    public static final int MAX_DESCRIPTION_LENGTH = 2047;

    public static final boolean NAME_REQUIRED = true; //DB RESTRICTIONS
    public static final boolean DESCRIPTION_REQUIRED = true; //DB RESTRICTIONS
    public static final boolean PRICE_REQUIRED = true;
    public static final boolean PHONE_REQUIRED = false;
    public static final boolean CITY_REQUIRED = false;
    public static final boolean CATEGORY_REQUIRED = false;
    public static final boolean IMAGES_REQUIRED = false;

    public static final double PRICE_ZERO = 0;

    public static final double PRICE_MIN = 1;

    public static final double PRICE_MAX = 9999999;
    public static int MIN_PHONE_LENGTH = 7;
    public static int MAX_PHONE_LENGTH = 14;

    private ItemConfig() {
    }
}
