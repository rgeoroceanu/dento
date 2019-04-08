package com.company.dento.ui.page.edit;

import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.Page;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasUrlParameter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class EditPage<T> extends Page implements Localizable, AfterNavigationObserver, HasUrlParameter<Long> {

    protected final Binder<T> binder = new Binder<>();
    protected final VerticalLayout contentLayout = new VerticalLayout();
    private final Button saveButton = new Button();
    private final Button discardButton = new Button();

    public EditPage(final DataService dataService) {
        super(dataService);

        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        final Div footerDiv = new Div();

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        discardButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(e -> this.save());
        discardButton.addClickListener(e -> this.discard());

        buttonsLayout.addClassName("edit-layout__footer-buttons");
        footerDiv.addClassName("edit-layout__footer");

        contentLayout.setSizeFull();
        contentLayout.setPadding(false);
        contentLayout.setMargin(false);
        contentLayout.add(footerDiv);

        buttonsLayout.add(discardButton, saveButton);
        footerDiv.add(buttonsLayout);

        this.setContent(contentLayout);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    public void localize() {
        super.localize();
        saveButton.setText(Localizer.getLocalizedString("save"));
        discardButton.setText(Localizer.getLocalizedString("cancel"));
    }

    protected abstract void reload();

    protected abstract void bindFields();

    protected abstract void save();

    protected  abstract void discard();
}
