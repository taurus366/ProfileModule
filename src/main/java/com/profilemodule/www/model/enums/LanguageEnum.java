package com.profilemodule.www.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageEnum {
    English("English", "en", "en_En"),
    Bulgarian("Bulgarian", "bg", "bg_BG"),
    Turkish("Turkish", "tr", "tr_TR");

    private final String name;
    private final String code;
    private final String locale;

}
