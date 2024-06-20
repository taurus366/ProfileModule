package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.profilemodule.www.shared.i18n.Intl;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "groups")
public class GroupEntity extends BaseEntity {
    public static final String SCOPE = "GROUP";
    public static final String VIEW_ROLE = SCOPE + "_VIEW";
    public static final String READ_ROLE = SCOPE + "_READ";
    public static final String UPDATE_ROLE = SCOPE + "_UPDATE";
    public static final String DELETE_ROLE = SCOPE + "_DELETE";
    public static final String ADD_ROLE = SCOPE + "_ADD";
    public static final String TITLE = Intl.getGroupList();
    public static final String VIEW_ROUTE = "group_list";
    public static final VaadinIcon icon = VaadinIcon.GROUP;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ScopeEntity> scopes = new HashSet<>();

    public static String getTranslateTitle() {
        return CustomI18nProvider.getTranslationStatic(TITLE);
    }
}
