package com.profilemodule.www.view.Impl.city;

import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.model.service.CityService;
import com.profilemodule.www.shared.grids.GridList;
import com.profilemodule.www.shared.model.dto.GridListDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CityListViewImpl extends VerticalLayout {

    public final String ADDED_USER_MESSAGE = "Successfully created new City";
    public final String UPDATED_USER_MESSAGE = "Successfully updated City";
    public final String DELETED_USER_MESSAGE = "Successfully deleted City";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    private final CityService cityService;
    private final CityViewImpl cityView;


    public CityListViewImpl(CityService cityService, CityViewImpl cityView) {
        this.cityService = cityService;
        this.cityView = cityView;
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
//        grid.addItemDoubleClickListener(event -> )


        reloadGrid(grid);
        return layout;
    }

    private void initBtns(Button reload, Button create, Grid<CityEntity> grid) {
        reload.addClickListener(event -> reloadGrid(grid));
        create.addClickListener(event -> cityView.initUI(-1L).open());
    }

    private void reloadGrid(Grid<CityEntity> grid) {
        List<CityEntity> list = cityService.getAll();
        grid.setItems(list);
    }



}
