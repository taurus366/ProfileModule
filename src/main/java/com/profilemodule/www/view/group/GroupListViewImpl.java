package com.profilemodule.www.view.group;

import com.profilemodule.www.model.service.GroupService;
import com.profilemodule.www.model.service.ScopeCleanService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class GroupListViewImpl extends VerticalLayout {

    public final String ADDED_GROUP_MESSAGE = "Successfully created new Group";
    public final String UPDATED_GROUP_MESSAGE = "Successfully updated Group";
    public final String DELETED_GROUP_MESSAGE = "Successfully deleted Group";
    public final int NOTIFY_DURATION = 5000;
    public final Notification.Position NOTIFY_POSITION = Notification.Position.MIDDLE;
    public final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    private final GroupService groupService;
    private final ScopeCleanService scopeCleanService;

    public GroupListViewImpl(GroupService groupService, ScopeCleanService scopeCleanService) {
        this.groupService = groupService;
        this.scopeCleanService = scopeCleanService;
    }
}
