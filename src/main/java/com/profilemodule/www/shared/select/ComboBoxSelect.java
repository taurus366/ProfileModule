package com.profilemodule.www.shared.select;

import com.profilemodule.www.model.dto.UserLanguageDTO;
import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.service.CountryService;
import com.profilemodule.www.shared.i18n.UserLocale;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.List;

public class ComboBoxSelect {


    public static ComboBox<CountryEntity> countryEntityComboBox(CountryService countryService) {
        ComboBox<CountryEntity> countryEntityComboBox = new ComboBox<>();
        countryEntityComboBox.setPageSize(10);
        countryEntityComboBox.setItems(query -> {

            if (query.getFilter().isPresent() && !query.getFilter().get().isEmpty()) {
                final String filterText = query.getFilter().get();

                final List<CountryEntity> allByCodeName = countryService.findAllByCodeName(filterText, query.getPage(), query.getPageSize());
                return allByCodeName.stream();
            }

            return countryService.findAll(query.getPage(), query.getPageSize()).stream();
        });
        //determine language of the person
        final UserLanguageDTO userLocale = UserLocale.getUserLocale();
        countryEntityComboBox.setItemLabelGenerator(item -> item.getName().get(userLocale.getLanguageId().intValue()));

        return countryEntityComboBox;
    }
}
