package com.profilemodule.www.shared.grids;

import com.profilemodule.www.shared.model.dto.NewStyleGridDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewStyleGrid {


    public static NewStyleGridDto initGrid(String... widthAndHeight) {

        Dialog dialog = initDialog(widthAndHeight);
        TabSheet tabSheet = initTabsheet();
        H3 title = initTitle();


        final List<Icon> fullAndClose = initFullAndClose(dialog, widthAndHeight);
       Icon fullIcon = fullAndClose.get(0);
       Icon closeIcon = fullAndClose.get(1);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        horizontalLayout.add(fullIcon, closeIcon);
        dialog.getHeader().add(title);
        dialog.getHeader().add(horizontalLayout);

        dialog.add(tabSheet);

        List<Button> btns = initBtns(dialog);
        HorizontalLayout layout = new HorizontalLayout();
        layout.getStyle().set("width", "100%");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        btns.forEach(layout::add);
        dialog.getFooter().add(layout);

        /**
         * for return:
         * 1. save, save and close
         * 2. h3 title
         * 3. tabSheet, dialog
         */
        return NewStyleGridDto.builder()
                .save(btns.get(1))
                .saveAndClose(btns.get(0))
                .title(title)
                .tabSheet(tabSheet)
                .dialog(dialog)
                .build();
    }

    private static List<Icon> initFullAndClose(Dialog dialog, String[] widthHeight) {
        // SIZE FULL/MIN
        final Icon fullIcon = VaadinIcon.MODAL.create();
        fullIcon.addClassName("pointer");
        fullIcon.addClickListener(event -> {
            if(dialog.hasClassName("full-screen")) {
                dialog.removeClassName("full-screen");
                removeOverlayStyles();
                dialog.setDraggable(true);
                dialog.setResizable(true);
                if(widthHeight.length > 0){
                    dialog.setWidth(widthHeight[0]);
                    dialog.setHeight(widthHeight[1]);
                }
                else {
                    dialog.setWidth("1000px");
                    dialog.setHeight("800px");
                }

            } else {
                dialog.addClassName("full-screen");
                dialog.setSizeUndefined();
                applyOverlayStyles();
                dialog.setDraggable(false);
                dialog.setResizable(false);
            }
        });

        // CLOSE DIALOG
        final Icon closeIcon = VaadinIcon.CLOSE.create();
        closeIcon.addClassName("pointer");
        closeIcon.setColor("red");
        closeIcon.addClickListener(event -> dialog.close());

        return List.of(fullIcon, closeIcon);
    }

    private static Dialog initDialog(String[] widthHeight) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("1000px");
        dialog.setHeight("800px");
        if(widthHeight.length > 0) {
            dialog.setWidth(widthHeight[0]);
            dialog.setHeight(widthHeight[1]);
        }

        return dialog;
    }

    private static TabSheet initTabsheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.getStyle()
                .set("height", "100%");

        return tabSheet;
    }

    private static void applyOverlayStyles() {
        UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').style.position = 'absolute';" +
                "document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').style.top = '0';" +
                "document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').style.left = '0';" +
                "document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').style.bottom = '0';" +
                "document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').style.right = '0';");
    }

    private static void removeOverlayStyles() {
        UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').shadowRoot.querySelector('[part=\"overlay\"]').removeAttribute('style');");
    }

    private static H3 initTitle() {
        H3 title = new H3();
        title.getStyle()
                .set("width", "100%")
                .set("position", "absolute")
                .set("text-align", "center");

        return title;
    }

    private static List<Button> initBtns(Dialog dialog) {

    Button saveBtn = new Button("Save");
        saveBtn.addClassName("pointer");
        saveBtn.getStyle()
                .set("background", "#7fb17f")
                .set("color", "white");
        saveBtn.addClickListener(event -> {
        });
    Button saveAndCloseBtn = new Button("Save and close");
        saveAndCloseBtn.addClassName("pointer");
        saveAndCloseBtn.getStyle()
                .set("background", "#7fb17f")
                .set("color", "white");

        saveAndCloseBtn.addClickListener(event -> dialog.close());

       return List.of(saveAndCloseBtn, saveBtn);
    }



}
