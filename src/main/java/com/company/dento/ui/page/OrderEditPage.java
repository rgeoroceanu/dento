package com.company.dento.ui.page;

import com.company.dento.model.business.Color;
import com.company.dento.model.business.Doctor;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "addOrder")
@Log4j2
public class OrderEditPage extends Page implements Localizable, AfterNavigationObserver {

    private final Tabs tabs;
    private final Tab generalTab;
    private final Tab teethTab;
    private final Button saveButton;
    private final Button discardButton;
    private final ComboBox<Doctor> doctorField;
    private final TextField patientField;
    private final ComboBox<Color> colorField;
    private final TextArea observationsField;
    private final DatePicker dateField;
    private final Upload cadField;
    private final Checkbox paidField;
    private final TextField partialSumField;

    public OrderEditPage(final DataService dataService) {
        super(dataService);
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout generalLayout = new HorizontalLayout();
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        final Div footerDiv = new Div();

        doctorField = new ComboBox<>();
        patientField = new TextField();
        colorField = new ComboBox<>();
        observationsField = new TextArea();
        dateField = new DatePicker();
        cadField = new Upload();
        paidField = new Checkbox();
        partialSumField = new TextField();

        generalTab = new Tab();
        teethTab = new Tab();
        tabs = new Tabs();
        saveButton = new Button();
        discardButton = new Button();
        saveButton.addClassName("dento-button-full");
        discardButton.addClassName("dento-button-simple");

        buttonsLayout.addClassName("edit-layout__footer-buttons");
        footerDiv.addClassName("edit-layout__footer");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setMargin(false);
        generalLayout.setSizeFull();
        generalLayout.setAlignItems(Alignment.END);
        buttonsLayout.add(discardButton, saveButton);
        footerDiv.add(buttonsLayout);
        tabs.add(generalTab, teethTab);
        layout.add(tabs, generalLayout, footerDiv);
        this.setContent(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        generalTab.setSelected(true);
    }

    @Override
    public void localize() {
        super.localize();
        generalTab.setLabel(Localizer.getLocalizedString("generalInformation"));
        teethTab.setLabel(Localizer.getLocalizedString("teeth"));
        saveButton.setText(Localizer.getLocalizedString("save"));
        discardButton.setText(Localizer.getLocalizedString("cancel"));
    }
}
