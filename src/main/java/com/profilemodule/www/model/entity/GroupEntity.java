package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class GroupEntity extends BaseEntity {
    public static final String SCOPE = "GROUP";
    public static final String VIEW_ROLE = SCOPE + "_VIEW";
    public static final String READ_ROLE = SCOPE + "_READ";
    public static final String UPDATE_ROLE = SCOPE + "_UPDATE";
    public static final String DELETE_ROLE = SCOPE + "_DELETE";
    public static final String ADD_ROLE = SCOPE + "_ADD";
    public static final String TITLE = "Group list";
    public static final String VIEW_ROUTE = "group_list";

    @Column(nullable = false)
    private String name;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<PermissionEntity> permissions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ScopeEntity> scopes = new HashSet<>();
}
