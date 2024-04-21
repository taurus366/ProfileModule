package com.profilemodule.www.shared.model.dto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.tabs.TabSheet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewStyleGridDto {

    private Button save;
    private Button saveAndClose;
    private H3 title;
    private TabSheet tabSheet;
    private Dialog dialog;
}
