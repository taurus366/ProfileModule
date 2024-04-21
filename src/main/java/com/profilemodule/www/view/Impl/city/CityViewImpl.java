package com.profilemodule.www.view.Impl.city;

import com.profilemodule.www.shared.grids.NewStyleGrid;
import com.profilemodule.www.shared.model.dto.NewStyleGridDto;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class CityViewImpl extends VerticalLayout {

    public Dialog initUI(Long id) {
        System.out.println("TEST!");
        final NewStyleGridDto newStyleGridDto = NewStyleGrid.initGrid();


        return newStyleGridDto.getDialog();
    }
}
