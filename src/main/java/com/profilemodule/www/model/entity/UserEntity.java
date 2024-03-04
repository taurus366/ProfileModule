package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    public static final String GROUP = "ADMIN";
    public static final String VIEW = GROUP + "_VIEW";
    public static final String READ = GROUP + "_READ";
    public static final String UPDATE = GROUP + "_UPDATE";
    public static final String DELETE = GROUP + "_DELETE";
    public static final String ADD = GROUP + "_ADD";
    public static final String NAME = "User list";

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String locale = "en_EN";

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<GroupEntity> groups = new HashSet<>();
}
