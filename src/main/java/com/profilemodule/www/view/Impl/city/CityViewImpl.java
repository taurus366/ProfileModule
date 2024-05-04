package com.profilemodule.www.view.Impl.city;

import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.repository.CountryRepository;
import com.profilemodule.www.model.service.CityService;
import com.profilemodule.www.model.service.CountryService;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.shared.grids.NewStyleGrid;
import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.profilemodule.www.shared.i18n.Intl;
import com.profilemodule.www.shared.model.dto.MultiLanguageTextFieldDTO;
import com.profilemodule.www.shared.model.dto.NewStyleGridDto;
import com.profilemodule.www.shared.multiLanguageFields.MultiLanguageTextField;
import com.profilemodule.www.shared.select.ComboBoxSelect;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class CityViewImpl extends VerticalLayout {

    private final CityService cityService;
    private final CountryService countryService;
    private final LanguageService languageService;
    private final CountryRepository countryRepository;

    public CityViewImpl(CityService cityService, CountryService countryService, LanguageService languageService, CountryRepository countryRepository) {
        this.cityService = cityService;
        this.countryService = countryService;
        this.languageService = languageService;
        this.countryRepository = countryRepository;
    }


    public Dialog initUI(Long id) {
        final Optional<CityEntity> cityEntity = cityService.get(id);






        final String title = String.format("%s #%d", CustomI18nProvider.getTranslationStatic(Intl.getCity()), id);
        final NewStyleGridDto newStyleGridDto = NewStyleGrid.initGrid(title);
        final TabSheet tabSheet = newStyleGridDto.getTabSheet();


        /** 1 TAB **/
        final MultiLanguageTextFieldDTO multiLanguageTextFieldDTO = MultiLanguageTextField.MultiLanguageTextField(cityEntity.isPresent() ? cityEntity.get().getName() : new HashMap<>(), languageService);
        multiLanguageTextFieldDTO.getTextField().setLabel(CustomI18nProvider.getTranslationStatic(Intl.getCity()));

        ComboBox<CountryEntity> countryEntityComboBox = initCountryComboBox();
//        countryEntityComboBox.setRenderer(new TextRenderer<>(item -> String.format("%s", item.getName())));
        countryEntityComboBox.setLabel(Intl.getCountry());
        cityEntity.ifPresent(entity -> countryEntityComboBox.setValue(entity.getCountry()));

        Tab mainTab = new Tab(CustomI18nProvider.getTranslationStatic(Intl.getMain()));
        VerticalLayout mainLayout = new VerticalLayout();

        mainLayout.add(countryEntityComboBox, multiLanguageTextFieldDTO.getHorizontalLayout());

        tabSheet.add(mainTab, mainLayout);


        return newStyleGridDto.getDialog();
    }
    private ComboBox<CountryEntity> initCountryComboBox() {
//        final List<CountryEntity> all = countryService.getAll();


        // Create a custom data provider for lazy loading
//        CallbackDataProvider<CountryEntity, Void> lazyDataProvider = new CallbackDataProvider<>(
//                query -> countryService.getCountries(query.getOffset(), query.getLimit()).stream(),
//                query -> countryService.countCountries()
//        );

//
//        ComboBox<CountryEntity> countryEntityComboBox = new ComboBox<>();
//        countryEntityComboBox.setAutoOpen(false);
//        countryEntityComboBox.setPageSize(10);
//        countryEntityComboBox.setItems(query -> {
//
//
//            if(query.getFilter().isPresent() && !query.getFilter().get().isEmpty()) {
//                final String filterText = query.getFilter().get();
//
//                final List<CountryEntity> allByCodeName = countryService.findAllByCodeName(filterText, query.getPage(), query.getPageSize());
//                return allByCodeName.stream();
//            }
//
//            return countryService.findAll(query.getPage(), query.getPageSize()).stream();
////                    .filter(countryEntity -> countryEntity.getName().get(1).toLowerCase().contains(query.getFilter().orElse("").toLowerCase()));
//        });
//        countryEntityComboBox.setItemLabelGenerator(item -> item.getName().get(1));
////        countryEntityComboBox.setItems(all.stream().map(CountryEntity::getName).toString());
//
//        return countryEntityComboBox;
        return ComboBoxSelect.countryEntityComboBox(countryService);
    }
}
