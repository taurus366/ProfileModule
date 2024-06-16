package com.profilemodule.www.view.Impl.language;

import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import jakarta.annotation.security.RolesAllowed;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@Component
@RolesAllowed({LanguageEntity.VIEW_ROLE})
public class LanguageListView extends VerticalLayout implements HasDynamicTitle {

    public final String UPDATED_LANGUAGE_MESSAGE = "Successfully updated Language";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    private final LanguageService languageService;
    private Grid<LanguageEntity> grid;

    public LanguageListView(LanguageService languageService) {
        this.languageService = languageService;
        add(initUI());
    }

    public VerticalLayout initUI() {
        VerticalLayout verticalLayout = new VerticalLayout();

        final List<Button> buttons = initBtns();

        HorizontalLayout layout = new HorizontalLayout();
        buttons.forEach(layout::add);
        verticalLayout.add(layout);

        final Grid<LanguageEntity> languageEntityGrid = initGrid();
        verticalLayout.add(languageEntityGrid);

        languageEntityGrid.addItemDoubleClickListener(event -> createUpdateItemDialog(event).open());


        return verticalLayout;
    }

    private List<Button> initBtns() {
        Button refreshBtn = new Button(VaadinIcon.ROTATE_LEFT.create(), event -> reloadGrid());

        return List.of(refreshBtn);
    }

    private Grid<LanguageEntity> initGrid() {
        grid = new Grid<>(LanguageEntity.class);
        reloadGrid();
        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            final Grid.Column<LanguageEntity> created = grid.getColumnByKey("created");
            created.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getCreated().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));

            final Grid.Column<LanguageEntity> modified = grid.getColumnByKey("modified");
            modified.setRenderer(new TextRenderer<>(item -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
                Instant instant = Instant.parse(item.getModified().toString());
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of(extendedClientDetails.getTimeZoneId()));
                return time.format(formatter);
            }));
        });

        return grid;
    }

    private Dialog createUpdateItemDialog(ItemDoubleClickEvent<LanguageEntity> language) {
        final Dialog updateItemDialog = getUpdateItemDialog(language.getItem());

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> updateItemDialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button saveBtn = new Button(VaadinIcon.CHECK.create(), event -> {
            final LanguageEntity item = language.getItem();
            item.setActive(isActive.getValue());
            item.setDefault(isDefault.getValue());
            languageService.save(item);
            cancelBtn.click();
            Notification.show(UPDATED_LANGUAGE_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            reloadGrid();
        });

        updateItemDialog.getFooter().add(saveBtn, cancelBtn);
        return updateItemDialog;
    }


    private TextField languageName;
    private TextField localeName;
    private TextField codeName;
    private Select<Boolean> isActive;
    private Select<Boolean> isDefault;
    private Dialog getUpdateItemDialog(LanguageEntity language) {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        languageName = new TextField();
        languageName.setLabel("Name");
        languageName.setValue(language.getLanguageEnum().getName());
        languageName.setEnabled(false);
        languageName.setPrefixComponent(VaadinIcon.FLAG.create());
        localeName = new TextField();
        localeName.setLabel("Locale");
        localeName.setValue(language.getLanguageEnum().getLocale());
        localeName.setEnabled(false);
        codeName = new TextField();
        codeName.setLabel("Code");
        codeName.setValue(language.getLanguageEnum().getCode());
        codeName.setEnabled(false);
        isActive = new Select<>();
        isActive.setLabel("Is active");
        isActive.setItems(true, false);
        isActive.setValue(language.isActive());
        isDefault = new Select<>();
        isDefault.setLabel("Is default");
        isDefault.setItems(true, false);
        isDefault.setValue(language.isDefault());


        layout.add(languageName, localeName, codeName, isActive, isDefault);
        dialog.add(layout);
        return dialog;
    }


    private void reloadGrid() {
        final List<LanguageEntity> all = languageService.getAll();

        grid.setItems(all);
    }

    @Override
    public String getPageTitle() {
        return CustomI18nProvider.getTranslationStatic(LanguageEntity.TITLE);
    }
}
