package com.profilemodule.www.shared.i18n;

import com.profilemodule.www.model.dto.UserLanguageDTO;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import java.util.concurrent.atomic.AtomicReference;

import static com.profilemodule.www.shared.i18n.CustomI18nProvider.user;

public class UserLocale {

    public static UserLanguageDTO getUserLocale() {
        final WrappedSession session = VaadinSession.getCurrent().getSession();
        AtomicReference<String> sessionLocale = new AtomicReference<>((String) session.getAttribute("locale"));
        AtomicReference<String> sessionLangId = new AtomicReference<>((String) session.getAttribute("lang_id"));

        if(sessionLocale.get() == null || sessionLocale.get().isEmpty()) {
            user.get()
                    .ifPresent(entity -> {
                        sessionLocale.set(entity.getLanguage().getLanguageEnum().getLocale());
                        session.setAttribute("locale", sessionLocale.get());
                    });
        }
        if(sessionLangId.get() == null || sessionLangId.get().isEmpty()) {
            user.get()
                    .ifPresent(entity -> {
                        sessionLangId.set(String.valueOf(entity.getLanguage().getId()));
                        session.setAttribute("lang_id", sessionLangId.get());
                    });
        }


        return UserLanguageDTO.builder()
                .languageId(Long.parseLong(sessionLangId.get()) - 1)
                .locale(sessionLocale.get())
                .build();
    }

    // It should be run after languages is changed.
    public static void ClearUserLocale() {
        final WrappedSession session = VaadinSession.getCurrent().getSession();
        session.removeAttribute("lang_id");
        session.removeAttribute("locale");
    }
}
