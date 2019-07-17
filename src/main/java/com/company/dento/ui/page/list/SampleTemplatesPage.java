package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.SampleTemplateSpecification;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.SampleTemplateEditPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;

@UIScope
@org.springframework.stereotype.Component
@Route(value = "admin/samples")
@Log4j2
public class SampleTemplatesPage extends ListPage<SampleTemplate, SampleTemplateSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public SampleTemplatesPage(final DataService dataService) {
	    super(SampleTemplate.class, dataService, "Probe");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addComponentColumn(this::createActiveComponent).setKey("active");

        addEditColumn();
        addRemoveColumn();

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));
        grid.setDetailColumns(grid.getColumns().get(2), grid.getColumns().get(3));

        filterButton.setVisible(false);

        initFilters();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
	}

    protected void confirmRemove(final SampleTemplate item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("sample")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "proba " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final SampleTemplateSpecification criteria = new SampleTemplateSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(SampleTemplateEditPage.class);
    }

    protected void edit(final SampleTemplate item) {
        UI.getCurrent().navigate(SampleTemplateEditPage.class, item.getId());
    }

    private Component createActiveComponent(final SampleTemplate item) {
        final Icon icon = new Icon(item.isActive() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isActive() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private void remove(final SampleTemplate item) {
	    dataService.softDeleteEntity(item.getId(), SampleTemplate.class);
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getId()), 3000, Notification.Position.BOTTOM_CENTER);
        refresh();
    }

    @Override
    protected InputStream createPrintContent() {
        return null;
    }

    protected void initFilters() { }
}
