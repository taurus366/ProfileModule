package com.profilemodule.www.shared.model.dto;

import com.profilemodule.www.shared.i18n.CountryFlag;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiLanguageTextFieldDTO implements HasValue.ValueChangeEvent<Map<Integer, String>> {

    private HorizontalLayout horizontalLayout;
    private TextField textField;
    private Map<Integer, String> data;
    private CountryFlag flag;

    @Override
    public HasValue<?, Map<Integer, String>> getHasValue() {
        return null;
    }

    @Override
    public boolean isFromClient() {
        return false;
    }

    @Override
    public Map<Integer, String> getOldValue() {
        return data;
    }

    @Override
    public Map<Integer, String> getValue() {
        return data;
    }
}
