package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.JobTemplateSpecification;
import com.company.dento.model.business.JobTemplate;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.JobTemplateEditPage;
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
@Route(value = "jobTemplates")
@Log4j2
public class JobTemplatesPage extends ListPage<JobTemplate, JobTemplateSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public JobTemplatesPage(final DataService dataService) {
	    super(JobTemplate.class, dataService);

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn("selectionType");
        grid.addColumn("standardPrice");

        grid.addComponentColumn(this::createActiveComponent).setKey("active");

        addEditColumn();
        addRemoveColumn();

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));
        grid.setDetailColumns(grid.getColumns().get(2), grid.getColumns().get(3));

        initFilters();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
	}

    protected void confirmRemove(final JobTemplate item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("order")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "lucrarea numarul " +  item.getId()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final JobTemplateSpecification criteria = new JobTemplateSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(JobTemplateEditPage.class);
    }

    protected void edit(final JobTemplate item) {
        UI.getCurrent().navigate(JobTemplateEditPage.class, item.getId());
    }

    private Component createActiveComponent(final JobTemplate item) {
        final Icon icon = new Icon(item.isActive() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isActive() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private void remove(final JobTemplate item) {
        try {
            dataService.deleteEntity(item.getId(), JobTemplate.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting job: {}", item.getId());
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
