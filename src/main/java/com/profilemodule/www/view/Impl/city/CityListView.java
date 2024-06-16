package com.profilemodule.www.view.Impl.city;

import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.service.CityService;
import com.profilemodule.www.model.service.CountryService;
import com.profilemodule.www.shared.grids.GridList;
import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.profilemodule.www.shared.i18n.Intl;
import com.profilemodule.www.shared.i18n.UserLocale;
import com.profilemodule.www.shared.model.dto.GridListDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

//@PageTitle(CityEntity.TITLE)
//@Route(value = CityEntity.VIEW_ROUTE)
@PermitAll
public class CityListView extends VerticalLayout implements HasDynamicTitle {

    public final String ADDED_USER_MESSAGE = "Successfully created new City";
    public final String UPDATED_USER_MESSAGE = "Successfully updated City";
    public final String DELETED_USER_MESSAGE = "Successfully deleted City";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    private final CityService cityService;
    private final CityView cityView;
    private final CountryService countryService;


    public CityListView(CityService cityService, CityView cityView, CountryService countryService) {
        this.cityService = cityService;
        this.cityView = cityView;
        this.countryService = countryService;
        add(initUI());
    }


    public VerticalLayout initUI() {
        VerticalLayout verticalLayout = initGrid();


        return verticalLayout;
    }

    private VerticalLayout initGrid() {
        GridListDto gridListDto = GridList.initGrid(CityEntity.class);
        VerticalLayout layout = gridListDto.getVerticalLayout();
        @SuppressWarnings("unchecked")
        Grid<CityEntity> grid = (Grid<CityEntity>) gridListDto.getGrid();
        initBtns(gridListDto.getReload(), gridListDto.getCreate(), grid);
        renderColumns(grid);



        reloadGrid(grid);
        return layout;
    }

    private void initBtns(Button reload, Button create, Grid<CityEntity> grid) {
        reload.addClickListener(event -> reloadGrid(grid));
        create.addClickListener(event -> cityView.initUI(-1L).open());
        grid.addItemDoubleClickListener(event -> cityView.initUI(event.getItem().getId()).open());
    }

    private void reloadGrid(Grid<CityEntity> grid) {
        List<CityEntity> list = cityService.getAll();
        grid.setItems(list);
    }

    private void renderColumns(Grid<CityEntity> grid) {
        final Long languageId = UserLocale.getUserLocale().getLanguageId();
        System.out.println(languageId);

        final Grid.Column<CityEntity> columnCountryId = grid.getColumnByKey(CityEntity.Fields.countryId);
        final Grid.Column<CityEntity> columnName = grid.getColumnByKey(CityEntity.Fields.name);

        columnCountryId.setRenderer(new TextRenderer<>(item -> {
            final Optional<CountryEntity> country = countryService.get(item.getCountryId());
            if(country.isPresent()) {
                return country.get().getName().get(languageId.intValue());
            }
            return item.getCountryId().toString();
        }));
        columnName.setRenderer(new TextRenderer<>(item1 -> item1.getName().get(languageId.intValue()))) ;
    }


    @Override
    public String getPageTitle() {
        final String translationStatic = CustomI18nProvider.getTranslationStatic(Intl.getCity());
        return translationStatic;
    }
}
