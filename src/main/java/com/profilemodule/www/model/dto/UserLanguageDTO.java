package com.profilemodule.www.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLanguageDTO {
    private String locale;
    private Long languageId;
}
