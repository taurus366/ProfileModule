package com.profilemodule.www.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "group")
public class GroupEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany()
    private List<PermissionEntity> permissions;


}
