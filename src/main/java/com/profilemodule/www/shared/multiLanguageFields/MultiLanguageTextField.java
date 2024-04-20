package com.profilemodule.www.shared.multiLanguageFields;

import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.shared.i18n.CountryFlag;
import com.profilemodule.www.shared.model.dto.MultiLanguageTextFieldDTO;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MultiLanguageTextField {

    public static MultiLanguageTextFieldDTO MultiLanguageTextField(Map<Integer, String> data, LanguageService languageService) {

        final List<LanguageEntity> allActiveLanguages = languageService.getAllByActive();

        AtomicInteger currentLanguageIndex = new AtomicInteger(1);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("width", "100%");


        TextField textField = initTextField();
        Section sectionTextFieldFlag = initSectionTextFieldFlag();

        CountryFlag flag = new CountryFlag(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().getCode(), false);
        Integer selectedLanguageId = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
        String dataText = data.getOrDefault(selectedLanguageId, "");

        textField.setValue(dataText);

        flag.addClickListener(event -> {
            if(currentLanguageIndex.get() + 1 < allActiveLanguages.size()) {
                flag.setCountry(allActiveLanguages.get(currentLanguageIndex.addAndGet(1)).getLanguageEnum().getCode());
            } else {
                currentLanguageIndex.set(0);
                flag.setCountry(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().getCode());
            }

            Integer selectedLanguageId2 = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
            String dataText2 = data.getOrDefault(selectedLanguageId2, ""); // Get the text associated with the selected language ID

            textField.setValue(dataText2);
        });

        textField.addValueChangeListener(event -> {
            Integer selectedLanguageId2 = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
            data.put(selectedLanguageId2, textField.getValue());

        });

        sectionTextFieldFlag.add(textField, flag);
        horizontalLayout.add(sectionTextFieldFlag);

        return MultiLanguageTextFieldDTO
                .builder()
                .textField(textField)
                .data(data)
                .horizontalLayout(horizontalLayout)
                .flag(flag)
                .build();
    }

    private static TextField initTextField() {
        TextField field = new TextField();
        field.getStyle()
                .set("width", "100%")
                .set("min-height", "58px");
        return field;
    }

    private static Section initSectionTextFieldFlag() {
        Section sectionTextFieldFlag = new Section();
        sectionTextFieldFlag.getStyle()
                .set("display", "flex")
                .set("width", "100%")
                .set("align-items", "center")
                .set("justify-content", "space-around");
        return sectionTextFieldFlag;
    }



}
