package com.profilemodule.www.shared.profileImg;

import com.profilemodule.www.model.entity.UserEntity;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class ProfileImage {

    public static StreamResource getImgStream(UserEntity entity) {
            StreamResource sr = new StreamResource("user", () -> new ByteArrayInputStream(entity.getImg()));
            sr.setContentType("image/png");
        return sr;
    }


//    UPLOAD
//       MultiFileMemoryBuffer buffer1 = new MultiFileMemoryBuffer();
//
//        Upload upload = new Upload(buffer1);
//        upload.setMaxFiles(1);
//        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
//        upload.setMaxFileSize(maxFileSizeInBytes);
//        upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");
//        upload.addSucceededListener(event -> {
//
//                    String attacFileName = event.getFileName();
//
//                    try {
//                        // The image can be jpg png or gif, but we store it always as png file in this example
//                        BufferedImage inputImage = ImageIO.read(buffer1.getInputStream(attacFileName));
//                        ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
//                        ImageIO.write(inputImage, "png", pngContent);
//
//                        final Optional<UserEntity> userEntity = authenticatedUser.get();
//                        userEntity.ifPresent(entity -> {
//                    entity.setImg(pngContent.toByteArray());
//                    userService.update(entity);
//                    Notification.show(UPLOADED_PICTURE_MESSAGE, NOTIFY_DURATION, NOTIFY_POSITION)
//                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//                            profileImage.setSrc(ProfileImage.getImgStream(entity));
//                            profileImage.setWidth("8rem");
//                            profileImage.setHeight("8rem");
//
//                        });
//
//                    } catch (IOException e) {
//                            e.printStackTrace();
//                    }
}
