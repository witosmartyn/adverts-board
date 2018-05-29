package com.witosmartyn.app.config.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * @author Vitalii Martyniuk
 *
 * Enumeration that defined Type  for populate database
 */
public enum Cities {
    KYIV,
    DNIPROPETROVSK,
    CHERNIHIV,
    KHARKIV,
    ZHYTOMYR,
    POLTAVA,
    KHERSON,
    ZAPORIZHIA,
    LUHANSK,
    DONETSK,
    VINNYTSIA,
    MYKOLAIV,
    KIROVOHRAD,
    SUMY,
    LVIV,
    CHERKASY,
    KHMELNYTSKYI,
    VOLYN,
    RIVNE,
    IVANO_FRANKIVSK,
    TERNOPIL,
    ZAKARPATTIA,
    CHERNIVTSI;

    /**
     * @return @List available values
     */
    public static List<String> valuesList() {
        final ArrayList<String> names = new ArrayList<>();
        Arrays.asList(values()).forEach(city -> names.add(city.name()));
        return names;
    }

}
