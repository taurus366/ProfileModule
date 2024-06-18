package com.profilemodule.www.shared.i18n;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.model.service.UserService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Component
//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class LanguageSelector {

    private final AuthenticatedUser user;

    private final LanguageService languageService;
    private final UserService userService;

    public ComboBox<LanguageEntity> getLanguageSelectorBox(String title, boolean isFlag) {
        ComboBox<LanguageEntity> languageSelector = new ComboBox<>();

        AtomicReference<Long> langId = new AtomicReference<>();

        user.get().ifPresent(entity -> {
            langId.set(entity.getLanguage().getId());
        });

        if(title != null) languageSelector.setLabel(title);

        final Collection<LanguageEntity> allActiveLanguages = languageService.getAllByActive();

        languageSelector.setItems(allActiveLanguages);
        languageSelector.setItemLabelGenerator(item -> item.getLanguageEnum().getName());
        if(isFlag)
            languageSelector.setRenderer(new ComponentRenderer<>(language -> {
                CountryFlag flag = new CountryFlag(language.getLanguageEnum().getCode(), false);
                flag.getStyle().set("width", "30px").set("height", "29px");

                Span label = new Span(language.getLanguageEnum().getName());

                // Combine the flag and label in a HorizontalLayout
                HorizontalLayout layout = new HorizontalLayout(flag, label);
                layout.setAlignItems(FlexComponent.Alignment.CENTER); // Adjust alignment if needed

                return layout;
            }));

        final LanguageEntity languageByLocale = allActiveLanguages.stream().filter(entity -> Objects.equals(entity.getId(), langId.get())).findFirst().get();

        languageSelector.setValue(languageByLocale);

        languageSelector.addValueChangeListener(event -> onChangeLang(event));

        return languageSelector;
    }

    private void onChangeLang(AbstractField.ComponentValueChangeEvent<ComboBox<LanguageEntity>, LanguageEntity> event) {
        user.get().ifPresent(entity -> {
            entity.setLanguage(event.getValue());
            userService.update(entity);
        });
       CustomI18nProvider.updateLocale(true);
    }
}
