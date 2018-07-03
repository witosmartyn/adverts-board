package com.witosmartyn.app.config.components;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.Role;
import com.witosmartyn.app.services.CategoryService;
import com.witosmartyn.app.services.RoleService;
import com.witosmartyn.app.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.Collection;
import java.util.Properties;

/**
 * @author Vitalii Martyniuk
 * Collection Spring beans
 */
@Configuration
@Log4j
@PropertySource("classpath:other.properties")
public class Beans  {
    @Autowired
    private RoleService roleService;

    @Autowired
    private Environment env;
    /**
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        log.info("initialize BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * @return UserDetailsService implementation
     */

    /**
     * @return ReloadableResourceBundleMessageSource
     * Configured for get rid of the third messages.properties that
     * always (once again) need maintained.
     * Configured for pickUp all messages from defined folders and append to 'all messages'
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(1);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames(
                //pages
                "classpath:/i18n/footer/messages",
                "classpath:/i18n/pages/messages",
                "classpath:/i18n/pages/search/messages",
                "classpath:/i18n/pages/titles/messages",
                "classpath:/i18n/pages/loginRegistration/messages",
                "classpath:/i18n/categories/messages",
                //validation
                "classpath:/i18n/validation/messages"); //
        return messageSource;
    }
    /**
     * @return Cached collection of all roles on the data base
     */

    @Bean
    public Collection<Role> availableRoles() {
        return roleService.findAll();
    }

    @Bean
    public String propCustomNameEnv() {
        final String property = env.getProperty("prop.test");
        log.info("#"+property+"###");
        return property;
    }
}
