package com.profilemodule.www.shared.model.dto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GridListDto {

    private VerticalLayout verticalLayout;

    private Button reload;
    private Button create;
    private Button createSaveBtn;

    private Grid<?> grid;
}
