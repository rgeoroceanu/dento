package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.ClinicSpecification;
import com.company.dento.model.business.Clinic;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.ClinicEditPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

import java.io.InputStream;

@UIScope
@org.springframework.stereotype.Component
@Secured(value = {"USER", "ADMIN"})
@Route(value = "clinics")
@Log4j2
public class ClinicsPage extends ListPage<Clinic, ClinicSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public ClinicsPage(final DataService dataService) {
	    super(Clinic.class, dataService, "Clinici");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn("town");
        grid.addColumn("email");
        grid.addColumn("phone");
        grid.addColumn("address");

        addEditColumn();
        addRemoveColumn();

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));
        grid.setDetailColumns(grid.getColumns().get(2), grid.getColumns().get(3), grid.getColumns().get(4));
        filterButton.setVisible(false);

        initFilters();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
	}

    protected void confirmRemove(final Clinic item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("order")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "clinica " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final ClinicSpecification criteria = new ClinicSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(ClinicEditPage.class);
    }

    protected void edit(final Clinic item) {
        UI.getCurrent().navigate(ClinicEditPage.class, item.getId());
    }

    private void remove(final Clinic item) {
        try {
            dataService.deleteEntity(item.getId(), Clinic.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting clinic: {}", item.getId());
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
