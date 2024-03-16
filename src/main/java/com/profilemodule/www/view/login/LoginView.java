package com.profilemodule.www.view.login;

import com.profilemodule.www.config.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
    private final AuthenticatedUser user;

    public LoginView(AuthenticatedUser user) {
        this.user = user;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();

        final LoginI18n.Header header = new LoginI18n.Header();
        i18n.setHeader(header);
        i18n.setAdditionalInformation(null);
        setI18n(i18n);
        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(user.get().isPresent()) {
            setOpened(false);
            beforeEnterEvent.forwardTo("user");
        }
        setError(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
