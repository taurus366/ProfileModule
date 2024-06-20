package com.profilemodule.www.model.entity;

import com.profilemodule.www.shared.i18n.CustomI18nProvider;
import com.profilemodule.www.shared.i18n.Intl;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Builder
@Table(name = "country_city")
public class CityEntity extends BaseEntity {
    public static final String SCOPE = "CITY_LIST";
    public static final String VIEW_ROLE = SCOPE + "_VIEW";
    public static final String READ_ROLE = SCOPE + "_READ";
    public static final String UPDATE_ROLE = SCOPE + "_UPDATE";
    public static final String DELETE_ROLE = SCOPE + "_DELETE";
    public static final String ADD_ROLE = SCOPE + "_ADD";
    public static final String TITLE = Intl.getCityList();
    public static final String VIEW_ROUTE = "city_list";
    public static final VaadinIcon icon = VaadinIcon.USERS;

//    @ManyToOne()
//    private CountryEntity country;

    @Column()
    private Long countryId;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, String> name = new HashMap<>();

    public static String getTranslateTitle() {
        return CustomI18nProvider.getTranslationStatic(TITLE);
    }
}
