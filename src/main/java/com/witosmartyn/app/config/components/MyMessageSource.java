package com.witosmartyn.app.config.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;


/**
 * @author Vitalii Martyniuk
 * <p>
 * Custom Wrapper  version with more easier message retrieval
 */
@Component
public class MyMessageSource extends ReloadableResourceBundleMessageSource {

    private MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public MyMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    /**
     * Initialization
     */
    @PostConstruct

    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
    }
    /**
     * @param code The code of message that needs to be retrieve.
     *
     * @return Localized String message, used locale from context.
     */
    public String get(String code) {
        return accessor.getMessage(code, LocaleContextHolder.getLocale());
    }
    /**
     * @param code The code of message that needs to be retrieve.
     * @param args The args[] that will be injected into the message.
     *
     * @return Localized String message with params, used locale from context.
     */
    public String get(String code, Object... args) {
        return accessor.getMessage(code, args, LocaleContextHolder.getLocale());
    }
    /**
     * @param code The code of message that needs to be retrieve.
     * @param locale The locale by which messages will be received
     *
     * @return Localized String message.
     */
    public String get(String code, Locale locale) {
        return accessor.getMessage(code, locale);
    }
    /**
     * @return String message
     * @param code The code of message that needs to be retrieve.
     * @param locale The locale by which messages will be received
     * @param args The args[] that will be injected into the message.
     *
     * @return Localized String message by Locale and with args[].
     */
    public String get(String code, Locale locale, Object... args) {
        return accessor.getMessage(code, locale);

    }

}
