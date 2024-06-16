package com.profilemodule.www.view.Impl.city;

import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.model.service.CityService;
import com.profilemodule.www.model.service.CountryService;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.shared.grids.NewStyleGridList;
import com.profilemodule.www.shared.model.dto.NewStyleGridListDto;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;

@PageTitle(CityEntity.TITLE)
//@Route(value = CityEntity.VIEW_ROUTE, absolute = true)
@PermitAll
public class CityListViewNewStyle extends VerticalLayout {

    private final CityService cityService;
    private final CountryService countryService;
    private final LanguageService languageService;


    public CityListViewNewStyle(CityService cityService, CountryService countryService, LanguageService languageService) {
        this.cityService = cityService;
        this.countryService = countryService;
        this.languageService = languageService;
        initUI();
    }


    public void initUI() {

//        final NewStyleGridListDto cityGridList  = NewStyleGridList.initGridList("test", CityEntity.class);
//
//        Dialog dialog = cityGridList.getDialog();
//        dialog.open();
//        add(dialog);


    }

}
