package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.LanguageEnum;
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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "language")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class LanguageEntity extends BaseEntity {
    public static final String SCOPE = "LANGUAGE";
    public static final String VIEW_ROLE = SCOPE + "_VIEW";
    public static final String READ_ROLE = SCOPE + "_READ";
    public static final String UPDATE_ROLE = SCOPE + "_UPDATE";
    public static final String DELETE_ROLE = SCOPE + "_DELETE";
    public static final String ADD_ROLE = SCOPE + "_ADD";
    public static final String TITLE = Intl.getLanguageList();
    public static final String VIEW_ROUTE = "language_list";
    public static final VaadinIcon icon = VaadinIcon.FLAG;

    @Enumerated(EnumType.ORDINAL)
    private LanguageEnum languageEnum;

    @Column
    private boolean active = false;

    @Column
    private boolean isDefault = false;

    public static String getTranslateTitle() {
        return CustomI18nProvider.getTranslationStatic(TITLE);
    }
}
