package com.profilemodule.www.view.Impl.profile;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.model.service.UserService;
import com.profilemodule.www.shared.i18n.LanguageSelector;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Component
public class ProfileViewImpl extends VerticalLayout {

    public final String UPDATED_USER_MESSAGE = "Successfully updated User";
    public final String UPLOADED_PICTURE_MESSAGE = "Successfully upload Picture";
    public final String ERROR_FILE_TYPE_MESSAGE = "INCORRECT FILE TYPE , ONLY COULD BE UPLOAD [png, jpg, jpeg]";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.BOTTOM_STRETCH;
    public static final String TITLE = "Profile";
    public static final String VIEW_ROUTE = "profile";

    private final AuthenticatedUser authenticatedUser;
    private final LanguageService languageService;
    private final UserService userService;
    private final AuthenticatedUser user;
    private final PasswordEncoder passwordEncoder;
    public ProfileViewImpl(AuthenticatedUser authenticatedUser, LanguageService languageService, UserService userService, AuthenticatedUser user, PasswordEncoder passwordEncoder) {
        this.authenticatedUser = authenticatedUser;
        this.languageService = languageService;
        this.userService = userService;
        this.user = user;
        this.passwordEncoder = passwordEncoder;
    }
    private Long userId;

    public VerticalLayout initUI() {
        VerticalLayout verticalLayout = new VerticalLayout();

        populateFields(verticalLayout);


        return verticalLayout;
    }

    private Dialog changePasswordDialog() {
        Dialog changePasswordDialog = new Dialog();
        changePasswordDialog.setHeaderTitle("Change password");

        VerticalLayout layout = new VerticalLayout();

        TextField newPassword = new TextField();
        newPassword.setLabel("New password");
        newPassword.setRequired(true);
        newPassword.setErrorMessage("Please fill the field");
        newPassword.setRequiredIndicatorVisible(true);
        layout.add(newPassword);

        TextField repeatPassword = new TextField();
        repeatPassword.setLabel("Repeat password");
        repeatPassword.setRequired(true);
        repeatPassword.setErrorMessage("Please fill the field");
        repeatPassword.setRequiredIndicatorVisible(true);
        layout.add(repeatPassword);

        Button saveBtn = new Button(VaadinIcon.CHECK.create(), event -> {
            newPassword.setInvalid(newPassword.getValue().isEmpty());

            if(repeatPassword.getValue().isEmpty() || !newPassword.getValue().equals(repeatPassword.getValue())) {
              if(repeatPassword.getValue().isEmpty()) repeatPassword.setErrorMessage("Please fill the field");
              else repeatPassword.setErrorMessage("Fields are not equal");
              repeatPassword.setInvalid(true);
            } else {
                repeatPassword.setInvalid(false);
                final Optional<UserEntity> userEntity = userService.get(userId);
                userEntity.ifPresent(entity -> {
                    entity.setPassword(passwordEncoder.encode(newPassword.getValue()));
                    userService.update(entity);
                    changePasswordDialog.close();
                    Notification.show(UPDATED_USER_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                });
            };
        });

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), event -> changePasswordDialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);


        changePasswordDialog.getFooter().add(saveBtn, cancelBtn);
        changePasswordDialog.add(layout);
        return changePasswordDialog;
    }

    private void populateFields(VerticalLayout verticalLayout) {
        LanguageSelector languageSelector = new LanguageSelector(authenticatedUser, languageService);
        final ComboBox<LanguageEntity> languageSelect = languageSelector.getLanguageSelectorBox("Language", true);

        verticalLayout.add(languageSelect);
        verticalLayout.setAlignSelf(Alignment.START, languageSelect);

        userImage(verticalLayout);

        TextField username = new TextField();
        username.setLabel("Username");
        verticalLayout.add(username);
        verticalLayout.setAlignSelf(Alignment.CENTER, username);

        TextField name = new TextField();
        name.setLabel("Names");
        verticalLayout.add(name);
        verticalLayout.setAlignSelf(Alignment.CENTER, name);

        FlexLayout flexLayout = new FlexLayout();
        flexLayout.setSizeFull();
        flexLayout.getStyle().set("gap", "10px");
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        flexLayout.setAlignItems(Alignment.CENTER);
        flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        NumberField phone = new NumberField();
        phone.setLabel("Phone number");
        flexLayout.add(phone);

        TextField email = new TextField();
        email.setLabel("Email address");
        flexLayout.add(email);


        verticalLayout.add(flexLayout);
        verticalLayout.setAlignSelf(Alignment.CENTER, flexLayout);

        Button saveBtnFields = new Button();
        saveBtnFields.setText("Save");
        saveBtnFields.addClickListener(event -> {
            final Optional<UserEntity> userEntity = userService.get(userId);
            if(userEntity.isPresent()) {
              if(username.getValue() != null) userEntity.get().setUsername(username.getValue());
              if(name.getValue() != null) userEntity.get().setName(name.getValue());
              if(phone.getValue() != null) {
                  String phoneNumber = String.format("%.0f", phone.getValue()).length() == 9 ?
                          "359" + String.format("%.0f", phone.getValue()):
                          String.format("%.0f", phone.getValue());
                  userEntity.get().setPhone(phoneNumber);
                  phone.setValue(Double.valueOf(phoneNumber));
              }
              if(email.getValue() != null) userEntity.get().setEmail(email.getValue());
              userService.update(userEntity.get());
                Notification.show(UPDATED_USER_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });
        verticalLayout.add(saveBtnFields);
        verticalLayout.setAlignSelf(Alignment.CENTER, saveBtnFields);


        Button newPassword = new Button();
        newPassword.setText("New password");
        newPassword.addClickListener(event -> changePasswordDialog().open());
        verticalLayout.add(newPassword);
        verticalLayout.setAlignSelf(Alignment.CENTER, newPassword);

        authenticatedUser.get().ifPresent(entity -> {
            userId = entity.getId();
            username.setValue(entity.getUsername());
            name.setValue(entity.getName());
            phone.setValue(entity.getPhone() != null ? Double.valueOf(entity.getPhone()) : null);
            email.setValue(entity.getEmail() != null ? entity.getEmail() : "");
        });
    }

    private void userImage(VerticalLayout layout) {
      FlexLayout layout1 = new FlexLayout();
        layout1.setSizeFull();
        layout1.getStyle().set("gap", "10px");
        layout1.setJustifyContentMode(JustifyContentMode.CENTER);
        layout1.setAlignItems(Alignment.CENTER);
        layout1.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        Image profileImage = new Image();
        profileImage.setWidth("8rem");
        profileImage.setHeight("8rem");
        final Optional<UserEntity> userEntity1 = authenticatedUser.get();
        userEntity1.ifPresent(entity -> {
            if(entity.getProfileImage() != null) profileImage.setSrc(entity.getProfileImage());
        });


        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.setAcceptedFileTypes(".png", ".jpg", ".jpeg");
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            try {
                // save to path
                String uploadDirectory = "MainModule/src/main/resources/META-INF/resources/media/images/"+userId+"/";
                Path directoryPath = Path.of(uploadDirectory);

                if(!Files.exists(directoryPath)) Files.createDirectories(directoryPath);

                Path filePath = directoryPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);


                inputStream.close();

                final Optional<UserEntity> userEntity = authenticatedUser.get();
                final String uri = "/media/images/"+userId+"/"+fileName;
                userEntity.ifPresent(entity -> {
                    entity.setProfileImage(uri);
                    userService.update(entity);
                    Notification.show(UPLOADED_PICTURE_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                });
//               /media/images/1/anonymous-mask-icon-22.jpg

                profileImage.setSrc(uri);

            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        upload.addFileRejectedListener(event1 -> {
            Notification.show(ERROR_FILE_TYPE_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
        upload.setDropLabel(new Span("jpeg, png, jpg"));
        upload.setUploadButton(new Button("Upload Picture"));
//        MainModule/src/main/resources/META-INF/resources/media/images

        layout1.add(profileImage, upload);

        layout.add(layout1);
        layout.setAlignSelf(Alignment.CENTER, layout1);


    }


}
