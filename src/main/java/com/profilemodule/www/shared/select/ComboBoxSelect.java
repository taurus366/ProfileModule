package com.profilemodule.www.shared.select;

import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.service.CountryService;
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
        countryEntityComboBox.setItemLabelGenerator(item -> item.getName().get(1));

        return countryEntityComboBox;
    }
}
