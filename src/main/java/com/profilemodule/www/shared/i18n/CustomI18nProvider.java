package com.profilemodule.www.shared.i18n;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.enums.LanguageEnum;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.model.service.UserService;
import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class CustomI18nProvider implements I18NProvider {


    private static String ERROR_MESSAGE = "Got language request for key : %s with error";

    private final LanguageService languageService;

    public static final String LOCATION_PREFIX = "i18n/translate";

    public CustomI18nProvider(LanguageService languageService) {
        this.languageService = languageService;
    }


    @Override
    public List<Locale> getProvidedLocales() {
        final List<Locale> allByActive = languageService.getAllByActive()
                .stream()
                .map(entity -> Locale.forLanguageTag(entity.getLanguageEnum().getLocale())).toList();
        return allByActive;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {

        if(key == null) {
            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
                    .warn(ERROR_MESSAGE);
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(LOCATION_PREFIX, Locale.forLanguageTag(locale.toString().split("_")[0]));

        String value;
        try {
            value = bundle.getString(key);
        } catch (MissingResourceException e) {
            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
                    .warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }

    @Override
    public String getTranslation(Object key, Locale locale, Object... params) {
        return I18NProvider.super.getTranslation(key, locale, params);
    }

    public static List<LanguageEntity> getActiveLanguages(LanguageService languageService) {

        final List<LanguageEntity> allByActive = languageService.getAllByActive();

        return allByActive;
    }

//    public static String getTranslationStatic(UserService userService, LanguageService languageService, String key, Locale locale, Object... params) {
//
//        if( key == null) {
//            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
//                    .warn("Got language request for key with null value!");
//            return "";
//        }
//
//        final ResourceBundle bundle = ResourceBundle.getBundle(LOCATION_PREFIX, Locale.forLanguageTag(locale.toString().split("_")[0]));
//
//        String value;
//        try {
//            value = bundle.getString(key);
//        } catch (final MissingResourceException e) {
//            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
//                    .warn("Missing resource", e);
//            return "!" + locale.getLanguage() + ": " + key;
//        }
//        if (params.length > 0) {
//            value = MessageFormat.format(value, params);
//        }
//        return value;
//
//    }

    public static String locale;
    public static String getTranslationStatic(String key, LanguageService languageService, AuthenticatedUser user) {


        if( key == null) {
            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
                    .warn("Got language request for key with null value!");
            return "";
        }

        user.get().ifPresent(entity -> {
            final LanguageEnum languageEnum = entity.getLanguage().getLanguageEnum();
            locale = languageEnum.getCode();
        });
        final ResourceBundle bundle = ResourceBundle.getBundle(LOCATION_PREFIX, Locale.forLanguageTag(locale));


        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
                    .warn("Missing resource", e);
            return "!" + locale + ": " + key;
        }
        return value;
    }
}
