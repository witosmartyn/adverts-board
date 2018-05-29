package com.witosmartyn.app.config.components;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vitalii Martyniuk
 * Collection Spring beans
 */
@Configuration
@Log4j
@PropertySource("classpath:icon-category-string-map.properties")
@ConfigurationProperties("")


public class CategoryIconProcessor {
    private final Map<String, String> icon = new HashMap<>();

    public Map<String, String> getIcon() {
        return icon;
    }

    @PostConstruct
    public void init() {
        if (log.isDebugEnabled()) {

            log.debug("matching icons and categories :" + icon);
        }
    }
}
