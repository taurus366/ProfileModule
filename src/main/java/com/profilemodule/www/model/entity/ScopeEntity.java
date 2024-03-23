package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "scope")
public class ScopeEntity extends BaseEntity {

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<PermissionEnum> permissions = new ArrayList<>();
}
