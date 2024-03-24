package com.profilemodule.www.view.Impl.profile;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewImpl extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;
    public ProfileViewImpl(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
