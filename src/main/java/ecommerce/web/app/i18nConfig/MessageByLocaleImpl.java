package ecommerce.web.app.i18nConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class MessageByLocaleImpl implements MessageByLocale {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Override
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle words
                = ResourceBundle.getBundle("classpath:messages", locale);
        String value = words.getString(id);
        return messageSource.getMessage(value,null,locale);
    }
}
