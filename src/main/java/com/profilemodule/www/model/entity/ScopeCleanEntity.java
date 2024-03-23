package com.profilemodule.www.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "scope_clear")
public class ScopeCleanEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;
}
