package com.profilemodule.www.model.entity;

import com.profilemodule.www.model.enums.LanguageEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "language")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageEntity extends BaseEntity {

    @Enumerated(EnumType.ORDINAL)
    private LanguageEnum languageEnum;

    @Column
    private boolean active = false;

    @Column
    private boolean isDefault = false;
}
