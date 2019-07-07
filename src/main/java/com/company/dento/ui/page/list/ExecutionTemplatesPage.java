package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.ExecutionTemplateSpecification;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.ExecutionTemplateEditPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

import java.io.InputStream;

@UIScope
@org.springframework.stereotype.Component
@Secured(value = {"USER", "ADMIN"})
@Route(value = "executionTemplates")
@Log4j2
public class ExecutionTemplatesPage extends ListPage<ExecutionTemplate, ExecutionTemplateSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public ExecutionTemplatesPage(final DataService dataService) {
	    super(ExecutionTemplate.class, dataService);

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn("standardPrice");

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

    protected void confirmRemove(final ExecutionTemplate item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("order")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "manopera " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final ExecutionTemplateSpecification criteria = new ExecutionTemplateSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(ExecutionTemplateEditPage.class);
    }

    protected void edit(final ExecutionTemplate item) {
        UI.getCurrent().navigate(ExecutionTemplateEditPage.class, item.getId());
    }

    private Component createActiveComponent(final ExecutionTemplate item) {
        final Icon icon = new Icon(item.isActive() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isActive() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private void remove(final ExecutionTemplate item) {
        try {
            dataService.deleteEntity(item.getId(), ExecutionTemplate.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting execution: {}", item.getId());
        }
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getId()), 3000, Notification.Position.BOTTOM_CENTER);
    }

    @Override
    protected InputStream createPrintContent() {
        return null;
    }

    protected void initFilters() { }
}
