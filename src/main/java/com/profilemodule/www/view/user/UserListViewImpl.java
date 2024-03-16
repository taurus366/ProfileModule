package com.profilemodule.www.view.user;

import com.profilemodule.www.model.entity.GroupEntity;
import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.repository.GroupRepository;
import com.profilemodule.www.model.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Transactional
public class UserListViewImpl extends VerticalLayout {

    public final String ADDED_USER_MESSAGE = "Successfully created new User";
    public final String UPDATED_USER_MESSAGE = "Successfully updated User";
    public final String DELETED_USER_MESSAGE = "Successfully deleted User";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;


    public UserListViewImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
    }


    public VerticalLayout initUI() {
        VerticalLayout verticalLayout = new VerticalLayout();

        final List<Button> buttons = initBtns();
        HorizontalLayout layout = new HorizontalLayout();
        buttons.forEach(layout::add);
        verticalLayout.add(layout);

        final Grid<UserEntity> userEntityGrid = initGrid();
        verticalLayout.add(userEntityGrid);

        userEntityGrid.addItemDoubleClickListener(event -> createUpdateItemDialog(event).open());

        return verticalLayout;
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
               else if(component instanceof PasswordField) {
                   if(((PasswordField) component).getValue().isEmpty() || ((PasswordField) component).getValue().isBlank()) {
                       isReady.set(false);
                       ((PasswordField) component).setInvalid(true);
                   } else {
                     ((PasswordField) component).setInvalid(false);
                   }
               }



            });
            if(isReady.get()) {
               UserEntity user = new UserEntity();
               user.setName(nameField.getValue());
               user.setUsername(usernameField.getValue());
               user.setPassword(passwordEncoder.encode(passwordField.getValue()));
               userRepository.save(user);
                dialog.close();
                Notification.show(ADDED_USER_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                reloadGrid();
            }
        });

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        dialog.getFooter().add(saveBtn, cancelBtn);
        return dialog;
    }

    private Dialog createUpdateItemDialog(ItemDoubleClickEvent<UserEntity> users) {

        final Dialog updateItemDialog = getUpdateItemDialog(users.getItem());

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> updateItemDialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button saveBtn = new Button(VaadinIcon.CHECK.create(), event -> {
            AtomicBoolean isReady = new AtomicBoolean(true);
            AtomicBoolean isPasswordChanged = new AtomicBoolean(false);
            updateItemDialog.getChildren().toList().get(0).getChildren().forEach(component -> {

                if(component instanceof TextField) {
                    if(((TextField) component).getValue().isEmpty() || ((TextField) component).getValue().isBlank()) {
                        isReady.set(false);
                        ((TextField) component).setInvalid(true);
                    } else {
                        ((TextField) component).setInvalid(false);
                    }
                }
                else if(component instanceof PasswordField) {
                    isPasswordChanged.set(!((PasswordField) component).getValue().isEmpty());
                }
            });
            if(isReady.get()) {
                UserEntity user = users.getItem();

                user.setName(nameField.getValue());
                user.setUsername(usernameField.getValue());
                user.setGroups(multiSelectComboBox.getSelectedItems());
                if(isPasswordChanged.get())
                    user.setPassword(passwordEncoder.encode(passwordField.getValue()));

                userRepository.save(user);
                cancelBtn.click();
                Notification.show(UPDATED_USER_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                reloadGrid();
            }
        });

        Button deleteBtn = new Button(VaadinIcon.TRASH.create(), event -> {

                try {
                    userRepository.deleteById(users.getItem().getId());
                } catch (Exception e) {
                    Notification.show(e.getMessage(), NOTIFY_DURATION, NOTIFY_POSITION)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

                cancelBtn.click();
            Notification.show(DELETED_USER_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            reloadGrid();
        });

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();

        final UserDetails principal = (UserDetails) securityContextHolder.getPrincipal();
        final UserEntity byUsername = userRepository.findByUsername(principal.getUsername());

        if(!Objects.equals(users.getItem().getId(), byUsername.getId()))
            updateItemDialog.getFooter().add(deleteBtn);

        updateItemDialog.getFooter().add(saveBtn, cancelBtn);
        return updateItemDialog;
    };

    private TextField nameField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Dialog getNewItemDialog() {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);


        nameField = new TextField("name");
        nameField.setRequired(true);
        nameField.setErrorMessage("Please fill the field");
        nameField.setRequiredIndicatorVisible(true);
        nameField.setPrefixComponent(VaadinIcon.USER.create());


        usernameField = new TextField("username");
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Please fill the field");
        usernameField.setRequiredIndicatorVisible(true);
        usernameField.setPrefixComponent(VaadinIcon.USER_CARD.create());

        passwordField = new PasswordField("password");
        passwordField.setRequired(true);
        passwordField.setErrorMessage("Please fill the field");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

        layout.add(nameField, usernameField, passwordField);
        dialog.add(layout);

        return dialog;
    }


    private MultiSelectComboBox<GroupEntity> multiSelectComboBox;
    private Dialog getUpdateItemDialog(UserEntity user) {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        nameField = new TextField();
        nameField.setRequired(true);
        nameField.setErrorMessage("Please fill the field");
        nameField.setRequiredIndicatorVisible(true);
        nameField.setPrefixComponent(VaadinIcon.USER.create());
        nameField.setValue(user.getName());

        usernameField = new TextField();
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Please fill the field");
        usernameField.setRequiredIndicatorVisible(true);
        usernameField.setPrefixComponent(VaadinIcon.USER_CARD.create());
        usernameField.setValue(user.getUsername());

        passwordField = new PasswordField();
        passwordField.setRequired(true);
        passwordField.setErrorMessage("Please fill the field");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

        final List<GroupEntity> allGroups = groupRepository.findAll();
        final List<GroupEntity> selectedList = allGroups
                .stream()
                .filter(u -> user.getGroups()
                        .stream()
                        .anyMatch(groupEntity -> Objects.equals(groupEntity.getId(), u.getId()))
                ).toList();

        multiSelectComboBox = new MultiSelectComboBox<>();
        multiSelectComboBox.setItems(allGroups);
        multiSelectComboBox.setRenderer(new TextRenderer<>(item -> String.format("%s", item.getName())));
        multiSelectComboBox.select(selectedList);
        multiSelectComboBox.setItemLabelGenerator(GroupEntity::getName);
//        final Set<GroupEntity> selectedItems = multiSelectComboBox.getSelectedItems();



        layout.add(nameField, usernameField, passwordField, multiSelectComboBox);
        dialog.add(layout);

        return dialog;
    }

    private Grid<UserEntity> grid;
    private Grid<UserEntity> initGrid() {
        grid = new Grid<>(UserEntity.class);
        reloadGrid();
        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            final Grid.Column<UserEntity> created = grid.getColumnByKey("created");
            created.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getCreated().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));

            final Grid.Column<UserEntity> modified = grid.getColumnByKey("modified");
            modified.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getModified().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));
        });



        final Grid.Column<UserEntity> password = grid.getColumnByKey("password");
        password.setVisible(false);

        final Grid.Column<UserEntity> locale = grid.getColumnByKey("locale");
        locale.setVisible(false);

        final Grid.Column<UserEntity> groups = grid.getColumnByKey("groups");
        groups.setVisible(false);
        return grid;
    }

    private void reloadGrid() {
        final List<UserEntity> all = userRepository.findAll();
        for (UserEntity userEntity : all) {
            Hibernate.initialize(userEntity.getGroups());
        }
        grid.setItems(all);
    }


}
