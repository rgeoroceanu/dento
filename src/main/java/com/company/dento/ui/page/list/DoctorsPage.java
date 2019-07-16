package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.DoctorSpecification;
import com.company.dento.model.business.Doctor;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.DoctorEditPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@UIScope
@org.springframework.stereotype.Component
@Route(value = "admin/doctors")
@Log4j2
public class DoctorsPage extends ListPage<Doctor, DoctorSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public DoctorsPage(final DataService dataService) {
	    super(Doctor.class, dataService, "Doctori");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("firstName");
        grid.addColumn("lastName");
        grid.addColumn("clinic.name");
        grid.addColumn("email");
        grid.addColumn("phone");

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

    protected void confirmRemove(final Doctor item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("order")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "doctorul Dr. " +  item.getLastName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final DoctorSpecification criteria = new DoctorSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(DoctorEditPage.class);
    }

    protected void edit(final Doctor item) {
        UI.getCurrent().navigate(DoctorEditPage.class, item.getId());
    }

    private void remove(final Doctor item) {
        try {
            dataService.deleteEntity(item.getId(), Doctor.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting doctor: {}", item.getId());
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
