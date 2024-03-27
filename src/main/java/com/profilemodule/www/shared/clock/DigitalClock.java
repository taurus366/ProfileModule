package com.profilemodule.www.shared.clock;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("digital-clock")
@JsModule("./themes/aioldsv2/clock/digitalClock.js")
//@JsModule("../../../../ProfileModule/frontend/themes/profile_module/clock/digitalClock.js")

@NpmPackage(value = "moment", version = "^2.29.1")
public class DigitalClock extends Component {

    public DigitalClock() {
    }
}

