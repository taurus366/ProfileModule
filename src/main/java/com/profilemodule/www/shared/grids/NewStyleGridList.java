package com.profilemodule.www.shared.grids;

import com.profilemodule.www.model.entity.BaseEntity;
import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.shared.model.dto.NewStyleGridDto;
import com.profilemodule.www.shared.model.dto.NewStyleGridListDto;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.ExtendedClientDetails;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class NewStyleGridList {

    public static String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    public static <T> NewStyleGridListDto initGridList(String stringTitle, Class<T> entity, String... widthAndHeight) {
        Dialog dialog = initDialog(widthAndHeight);
//        TabSheet tabSheet = initTabsheet();
        H3 title = initTitle(stringTitle);

        final List<Icon> fullAndClose = initFullAndClose(dialog, widthAndHeight);
        Icon fullIcon = fullAndClose.get(0);
        Icon closeIcon = fullAndClose.get(1);

        // HEADER
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        horizontalLayout.add(fullIcon, closeIcon);
        dialog.getHeader().add(title);
        dialog.getHeader().add(horizontalLayout);

        // BODY
        Grid<T> grid = initGrid(entity);
        VerticalLayout verticalLayout = new VerticalLayout();
        final List<Button> buttons = initBtns();
        HorizontalLayout horizontalLayoutBtns = new HorizontalLayout();
        buttons.forEach(horizontalLayoutBtns::add);
        verticalLayout.add(horizontalLayoutBtns, grid);

        dialog.add(verticalLayout);

        return NewStyleGridListDto.builder()
                .verticalLayout(verticalLayout)
                .title(title)
                .dialog(dialog)
                .reload(buttons.get(1))
                .create(buttons.get(0))
                .createSaveBtn(createNewDialogSaveBtn)
                .grid(grid)
                .build();
    }

    private static Button createNewDialogSaveBtn = new Button(VaadinIcon.CHECK.create());

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
//    private static TabSheet initTabsheet() {
//        TabSheet tabSheet = new TabSheet();
//        tabSheet.getStyle()
//                .set("height", "100%");
//
//        return tabSheet;
//    }
    private static H3 initTitle(String title) {
        H3 titleInit = new H3(title);
        titleInit.getStyle()
                .set("width", "100%")
                .set("position", "absolute")
                .set("text-align", "center");

        return titleInit;
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

//    private static List<Button> initBtns(Dialog dialog) {
//
//        Button saveBtn = new Button("Save");
//        saveBtn.addClassName("pointer");
//        saveBtn.getStyle()
//                .set("background", "#7fb17f")
//                .set("color", "white");
//        saveBtn.addClickListener(event -> {
//        });
//        Button saveAndCloseBtn = new Button("Save and close");
//        saveAndCloseBtn.addClassName("pointer");
//        saveAndCloseBtn.getStyle()
//                .set("background", "#7fb17f")
//                .set("color", "white");
//
//        saveAndCloseBtn.addClickListener(event -> dialog.close());
//
//        return List.of(saveAndCloseBtn, saveBtn);
//    }

    private static <T> Grid<T> initGrid(Class <T> entity) {
        Grid<T> grid = new Grid<T>(entity);

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            final Grid.Column<T> created = grid.getColumnByKey("created");
///           OLD USE
//           created.setRenderer(new TextRenderer<>(item -> {
//               DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
//               Instant instant = Instant.parse(((BaseEntity)item).getCreated().toString());
//               LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
//               return time.format(formatter);
//           }));

            final ItemLabelGenerator<T> labelGeneratorCreated = (ItemLabelGenerator<T>) getDate(extendedClientDetails, BaseEntity.class, "getCreated");
            final Renderer<T> createdRendererCreated = new TextRenderer<>(labelGeneratorCreated);
            created.setRenderer(createdRendererCreated);
            created.setTooltipGenerator(labelGeneratorCreated);

            final Grid.Column<T> modified = grid.getColumnByKey("modified");
///           OLD USE
//           modified.setRenderer(new TextRenderer<>(item -> {
//               DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
//                   Instant instant = Instant.parse(((BaseEntity)item).getModified().toString());
//               LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
//               return time.format(formatter);
//           }));
            final ItemLabelGenerator<T> labelGeneratorModified = (ItemLabelGenerator<T>) getDate(extendedClientDetails, BaseEntity.class, "getCreated");
            final Renderer<T> createdRendererModified = new TextRenderer<>(labelGeneratorModified);
            modified.setRenderer(createdRendererModified);
            modified.setTooltipGenerator(labelGeneratorModified);
        });

        return grid;
    }

    private static <T> ItemLabelGenerator<T> getDate(ExtendedClientDetails extendedClientDetails, Class<T> entityClass, String methodName) {
        return item -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);

            // Use reflection to dynamically retrieve the method value from the entity
            try {
                Method method = entityClass.getMethod(methodName);
                Instant instant = (Instant) method.invoke(item);
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error while getting property value", e);
            }
        };
    }

    private static List<Button> initBtns() {
//        Button createNewItemBtn = new Button(VaadinIcon.PLUS.create(), event -> createNewItemDialog().open());
        Button createNewItemBtn = new Button(VaadinIcon.PLUS.create());
//        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create(), event -> reloadGrid());
        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create());

        return List.of(createNewItemBtn, refreshBtn);
    }

}
