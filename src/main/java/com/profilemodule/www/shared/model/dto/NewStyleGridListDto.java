package com.profilemodule.www.shared.model.dto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewStyleGridListDto {
    private VerticalLayout verticalLayout;
    private H3 title;
    private Dialog dialog;
    private Button reload;
    private Button create;
    private Button createSaveBtn;

    private Grid<?> grid;
}
