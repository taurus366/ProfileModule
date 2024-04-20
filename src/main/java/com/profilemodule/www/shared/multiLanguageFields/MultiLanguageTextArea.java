package com.profilemodule.www.shared.multiLanguageFields;

import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.service.LanguageService;
import com.profilemodule.www.shared.i18n.CountryFlag;
import com.profilemodule.www.shared.model.dto.MultiLanguageTextAreaDTO;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MultiLanguageTextArea {


    public static MultiLanguageTextAreaDTO MultiLanguageTextArea(Map<Integer, String> data, LanguageService languageService) {

        final List<LanguageEntity> allActiveLanguages = languageService.getAllByActive();

        AtomicInteger currentLanguageIndex = new AtomicInteger(0);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("width", "100%");

        TextArea textArea = initTextArea();
        Section sectionTextAreaFlag = initSectionTextAreaFlag();

        CountryFlag flag = new CountryFlag(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().getCode(), false);
        Integer selectedLanguageId = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
        String dataText = data.getOrDefault(selectedLanguageId, "");

        textArea.setValue(dataText);

        flag.addClickListener(event -> {
           if(currentLanguageIndex.get() + 1 < allActiveLanguages.size()) {
               flag.setCountry(allActiveLanguages.get(currentLanguageIndex.addAndGet(1)).getLanguageEnum().getCode());
           } else {
             currentLanguageIndex.set(0);
             flag.setCountry(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().getCode());
           }

            Integer selectedLanguageId2 = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
            String dataText2 = data.getOrDefault(selectedLanguageId2, ""); // Get the text associated with the selected language ID

            textArea.setValue(dataText2);
        });

        textArea.addValueChangeListener(event -> {
            Integer selectedLanguageId2 = Integer.parseInt(String.valueOf(allActiveLanguages.get(currentLanguageIndex.get()).getLanguageEnum().ordinal()));
            data.put(selectedLanguageId2, textArea.getValue());

        });

        sectionTextAreaFlag.add(textArea, flag);
        horizontalLayout.add(sectionTextAreaFlag);

       return MultiLanguageTextAreaDTO
                .builder()
                .textArea(textArea)
                .data(data)
                .horizontalLayout(horizontalLayout)
                .flag(flag)
                .build();
    }



    private static TextArea initTextArea() {
        TextArea textArea = new TextArea();
        textArea.getStyle()
                .set("width", "100%")
//                .set("max-height", "58px")
                .set("min-height", "58px");
        return textArea;
    }

    private static Section initSectionTextAreaFlag() {
        Section sectionTextAreaAndFlag = new Section();
        sectionTextAreaAndFlag.getStyle()
                .set("display", "flex")
                .set("width", "100%")
                .set("align-items", "center")
                .set("justify-content", "space-around");
        return sectionTextAreaAndFlag;
    }
}
