package com.profilemodule.www.shared.grids;

import com.profilemodule.www.model.entity.BaseEntity;
import com.profilemodule.www.shared.model.dto.GridListDto;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.ExtendedClientDetails;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings("unchecked")
public class GridList {

    public static String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

   public static <T> GridListDto initGrid(Class<T> entity) {
       VerticalLayout layout = new VerticalLayout();
        Grid<T> grid = new Grid<T>(entity);
        /// by default change times only for [modified, created] columns;
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

       final List<Button> buttons = initBtns();
       HorizontalLayout horizontalLayout = new HorizontalLayout();
       buttons.forEach(horizontalLayout::add);
       layout.add(horizontalLayout, grid);

       return GridListDto.builder()
                 .verticalLayout(layout)
                 .reload(buttons.get(1))
                 .create(buttons.get(0))
                 .createSaveBtn(createNewDialogSaveBtn)
                 .grid(grid)
                 .build();
    }
    private static Button createNewDialogSaveBtn = new Button(VaadinIcon.CHECK.create());
    private static Dialog createNewItemDialog() {
       Dialog dialog = getNewItemDialog();

       Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        dialog.getFooter().add(createNewDialogSaveBtn, cancelBtn);
       return dialog;
    }

    private static Dialog getNewItemDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setCloseOnOutsideClick(false);

        return dialog;
    }

    private static List<Button> initBtns() {
//        Button createNewItemBtn = new Button(VaadinIcon.PLUS.create(), event -> createNewItemDialog().open());
        Button createNewItemBtn = new Button(VaadinIcon.PLUS.create());
//        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create(), event -> reloadGrid());
        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create());

        return List.of(createNewItemBtn, refreshBtn);
    }

    public static <T> ItemLabelGenerator<T> getDate(ExtendedClientDetails extendedClientDetails, Class<T> entityClass, String methodName) {
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



}
