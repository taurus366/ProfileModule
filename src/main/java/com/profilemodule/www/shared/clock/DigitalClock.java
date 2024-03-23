package com.profilemodule.www.shared.clock;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("digital-clock")
@JsModule("./themes/aiolds/clock/digitalClock.js")
@NpmPackage(value = "moment", version = "^2.29.1")
public class DigitalClock extends Component {


    public DigitalClock() {
    }
}
