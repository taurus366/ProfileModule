package com.profilemodule.www.view.Impl.profile;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewImpl extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private final AuthenticatedUser user;
    public ProfileViewImpl(AuthenticatedUser authenticatedUser, AuthenticatedUser user) {
        this.authenticatedUser = authenticatedUser;
        this.user = user;
    }


}
