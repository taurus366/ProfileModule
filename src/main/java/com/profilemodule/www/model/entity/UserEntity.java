package com.profilemodule.www.model.entity;

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

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldNameConstants
@Table(name = "users")
public class UserEntity extends BaseEntity {
    public static final String SCOPE = "USER_LIST";
    public static final String VIEW_ROLE = SCOPE + "_VIEW";
    public static final String READ_ROLE = SCOPE + "_READ";
    public static final String UPDATE_ROLE = SCOPE + "_UPDATE";
    public static final String DELETE_ROLE = SCOPE + "_DELETE";
    public static final String ADD_ROLE = SCOPE + "_ADD";
    public static final String TITLE = Intl.getUserList();
    public static final String VIEW_ROUTE = "user_list";
    public static final VaadinIcon icon = VaadinIcon.USERS;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column
    private String phone;

//    @Column
//    private String profileImage;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] img;

    @ManyToOne(fetch = FetchType.EAGER)
    private LanguageEntity language;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<GroupEntity> groups = new HashSet<>();

    public static String getTranslateTitle() {
        return CustomI18nProvider.getTranslationStatic(TITLE);
    }
}
