package com.profilemodule.www.shared.i18n;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
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




    /// WORKS
    public static AuthenticatedUser user;
    public static String getTranslationStatic(String key) {

        if(key == null) {
            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
                    .warn("Got language request for key with null value!");
            return "";
        }

        final WrappedSession session = VaadinSession.getCurrent().getSession();
        AtomicReference<String> sessionLocale = new AtomicReference<>((String) session.getAttribute("locale"));

        if(sessionLocale.get() == null || sessionLocale.get().isEmpty()) {
            user.get()
                    .ifPresent(entity -> {
                       sessionLocale.set(entity.getLanguage().getLanguageEnum().getLocale());
                        session.setAttribute("locale", sessionLocale.get());
                    });
        }


        final ResourceBundle bundle = ResourceBundle.getBundle(LOCATION_PREFIX, Locale.forLanguageTag(sessionLocale.get().split("_")[0]));


        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
//            LoggerFactory.getLogger(CustomI18nProvider.class.getName())
//                    .warn("Missing resource", e);
//            return "!" + sessionLocale + ": " + key;
            return key;
        }
        return value;
    }

    public static void updateLocale(boolean refreshPage) {
        final WrappedSession session = VaadinSession.getCurrent().getSession();
        AtomicReference<String> sessionLocale = new AtomicReference<>((String) session.getAttribute("locale"));
            user.get()
                    .ifPresent(entity -> {
                        sessionLocale.set(entity.getLanguage().getLanguageEnum().getLocale());
                        session.setAttribute("locale", sessionLocale.get());
                    });
            if(refreshPage) refreshPage();
    }

    private static void refreshPage() {
        UI.getCurrent().getPage().reload();
    }

    /// WORKS
}
