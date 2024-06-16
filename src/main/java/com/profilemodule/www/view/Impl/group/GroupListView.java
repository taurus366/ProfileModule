package com.profilemodule.www.view.Impl.group;

import com.profilemodule.www.model.entity.GroupEntity;
import com.profilemodule.www.model.entity.ScopeCleanEntity;
import com.profilemodule.www.model.entity.ScopeEntity;
import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.model.service.GroupService;
import com.profilemodule.www.model.service.ScopeCleanService;
import com.profilemodule.www.model.service.ScopeService;
import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//@Component

@Transactional
@RolesAllowed({GroupEntity.VIEW_ROLE})
public class GroupListView extends VerticalLayout implements HasDynamicTitle {

    public final String ADDED_GROUP_MESSAGE = "Successfully created new Group";
    public final String UPDATED_GROUP_MESSAGE = "Successfully updated Group";
    public final String DELETED_GROUP_MESSAGE = "Successfully deleted Group";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    public final String NAMEFIELD_ERROR_MESSAGE = "Please fill the field";

    private final GroupService groupService;
    private final ScopeCleanService scopeCleanService;
    private final ScopeService scopeService;

    private Grid<GroupEntity> grid;

    public GroupListView(GroupService groupService, ScopeCleanService scopeCleanService, ScopeService scopeService) {
        this.groupService = groupService;
        this.scopeCleanService = scopeCleanService;
        this.scopeService = scopeService;
        add(initUI());
    }

    public VerticalLayout initUI() {
        VerticalLayout verticalLayout = new VerticalLayout();

        final List<Button> buttons = initBtns();

        HorizontalLayout layout = new HorizontalLayout();
        buttons.forEach(layout::add);
        verticalLayout.add(layout);

        final Grid<GroupEntity> groupEntityGrid = initGrid();
        verticalLayout.add(groupEntityGrid);

        groupEntityGrid.addItemDoubleClickListener(event -> createUpdateItemDialog(event).open());


        return verticalLayout;
    }

    private Dialog createUpdateItemDialog(ItemDoubleClickEvent<GroupEntity> group) {

        final Dialog updateItemDialog = getUpdateItemDialog(group.getItem());

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> updateItemDialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button saveBtn = new Button(VaadinIcon.CHECK.create(), event -> {
            AtomicBoolean isReady = new AtomicBoolean(true);
            updateItemDialog.getChildren().toList().get(0).getChildren().forEach(component -> {

                if(component instanceof TextField) {
                    if(((TextField) component).getValue().isEmpty() || ((TextField) component).getValue().isBlank()) {
                        isReady.set(false);
                        ((TextField) component).setInvalid(true);
                    } else {
                        ((TextField) component).setInvalid(false);
                    }
                }
            });
            if(isReady.get()) {
               GroupEntity groupEntity = group.getItem();

                groupEntity.setName(nameField.getValue());
                groupEntity.setScopes(new HashSet<>());
                groupService.save(groupEntity);

                for (Map.Entry<String, MultiSelectListBox<String>> boxEntry : boxMap.entrySet()) {
                    final String key = boxEntry.getKey();
                    final MultiSelectListBox<String> value = boxEntry.getValue();

                    if(!value.getSelectedItems().isEmpty()) {
                        ScopeEntity scope = new ScopeEntity();
                        scope.setName(key);
                        scope.setPermissions(value.getSelectedItems().stream().map(PermissionEnum::valueOf).toList());
                        scopeService.save(scope);
                        groupEntity.getScopes().add(scope);
                    }
                }
                groupService.save(groupEntity);
                cancelBtn.click();
                Notification.show(UPDATED_GROUP_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                reloadGrid();
            }
        });

        Button deleteBtn = new Button(VaadinIcon.TRASH.create(), event -> {

            try {
                groupService.deleteById(group.getItem().getId());
            } catch (Exception e) {
                Notification.show(e.getMessage(), NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

            cancelBtn.click();
            Notification.show(DELETED_GROUP_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            reloadGrid();
        });


        updateItemDialog.getFooter().add(deleteBtn, saveBtn, cancelBtn);
        return updateItemDialog;
    };

    Map<String, MultiSelectListBox<String>> boxMap;
    private Dialog getUpdateItemDialog(GroupEntity group) {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        nameField = new TextField();
        nameField.setRequired(true);
        nameField.setErrorMessage("Please fill the field");
        nameField.setRequiredIndicatorVisible(true);
        nameField.setPrefixComponent(VaadinIcon.GROUP.create());
        nameField.setValue(group.getName());

        Accordion accordion = new Accordion();
        final Set<ScopeEntity> groupScopes = group.getScopes();


        boxMap = new HashMap<>();
        List<String> addedScopes = new ArrayList<>();

        for (ScopeEntity scope : groupScopes) {

            MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
            listBox.setItems(
                    Arrays.stream(PermissionEnum.values()).map(Enum::name).toList()
            );

            final HorizontalLayout horizontalLayout = new HorizontalLayout();



            final List<PermissionEnum> permissions = scope.getPermissions();
            for (PermissionEnum permission : permissions) {
                listBox.select(permission.name());
            }
                boxMap.put(scope.getName(), listBox);

            horizontalLayout.add(listBox);
            addedScopes.add(scope.getName());
            accordion.add(scope.getName(), horizontalLayout);
        }

        final List<ScopeCleanEntity> allCleanScopes = scopeCleanService.getAll();
        for (ScopeCleanEntity cleanScope : allCleanScopes) {
            if(!addedScopes.contains(cleanScope.getName())) {
                MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
                listBox.setItems(
                        Arrays.stream(PermissionEnum.values()).map(Enum::name).toList()
                );

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.add(listBox);

                boxMap.put(cleanScope.getName(), listBox);
                accordion.add(cleanScope.getName(), horizontalLayout);
            }
        }

        layout.add(nameField, accordion);
        dialog.add(layout);

        return dialog;
    }


    private List<Button> initBtns() {
        Button createNewItemBtn = new Button(VaadinIcon.PLUS.create(), event -> createNewItemDialog().open());
        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create(), event -> reloadGrid());

        return List.of(createNewItemBtn, refreshBtn);
    }

    private Dialog createNewItemDialog() {
        final Dialog dialog = getNewItemDialog();

        Button saveBtn = new Button(VaadinIcon.CHECK.create(), event -> {
            AtomicBoolean isReady = new AtomicBoolean(true);
            dialog.getChildren().toList().get(0).getChildren().forEach(component -> {


                if(component instanceof TextField) {
                    if(((TextField) component).getValue().isEmpty() || ((TextField) component).getValue().isBlank()) {
                        isReady.set(false);
                        ((TextField) component).setInvalid(true);
                    } else {
                        ((TextField) component).setInvalid(false);
                    }
                }
            });
            if(isReady.get()) {
                GroupEntity groupEntity = new GroupEntity();
                groupEntity.setName(nameField.getValue());
                groupService.save(groupEntity);
                dialog.close();
                Notification.show(ADDED_GROUP_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                reloadGrid();
            }
        });

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        dialog.getFooter().add(saveBtn, cancelBtn);
        return dialog;
    }


    private TextField nameField;
    private Dialog getNewItemDialog() {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);


        nameField = new TextField("name");
        nameField.setRequired(true);
        nameField.setErrorMessage(NAMEFIELD_ERROR_MESSAGE);
        nameField.setRequiredIndicatorVisible(true);
        nameField.setPrefixComponent(VaadinIcon.GROUP.create());

        layout.add(nameField);
        dialog.add(layout);

        return dialog;
    }

    private Grid<GroupEntity> initGrid() {
        grid = new Grid<>(GroupEntity.class);
        reloadGrid();
        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            final Grid.Column<GroupEntity> created = grid.getColumnByKey("created");
            created.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getCreated().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));

            final Grid.Column<GroupEntity> modified = grid.getColumnByKey("modified");
            modified.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getModified().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));
        });

        final Grid.Column<GroupEntity> password = grid.getColumnByKey("scopes");
        password.setVisible(false);

        return grid;
    }

    private void reloadGrid() {
        final List<GroupEntity> all = groupService.getAll();

        grid.setItems(all);
    }

    @Override
    public String getPageTitle() {
        return CustomI18nProvider.getTranslationStatic(GroupEntity.TITLE);
    }
}
