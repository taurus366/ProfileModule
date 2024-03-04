package com.profilemodule.www.view.user;

import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Transactional
public class UserListImpl extends VerticalLayout {

    public final static String VIEW = "user_list";
    private static final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";


    private final UserRepository userRepository;


    public UserListImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public com.vaadin.flow.component.Component initUI() {


        Grid<UserEntity> grid = new Grid<>(UserEntity.class);
        final List<UserEntity> all = userRepository.findAll();
        for (UserEntity userEntity : all) {
            Hibernate.initialize(userEntity.getGroups());
        }

        grid.setItems(all);

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


}
