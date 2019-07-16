package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.ToothOptionSpecification;
import com.company.dento.model.business.ToothOption;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.ToothOptionEditPage;
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
@Route(value = "admin/tooth_options")
@Log4j2
public class ToothOptionsPage extends ListPage<ToothOption, ToothOptionSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public ToothOptionsPage(final DataService dataService) {
	    super(ToothOption.class, dataService, "Opțiuni Dinți");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn("abbreviation");
        grid.addColumn("displayColumn");
        grid.addColumn(o -> o.getSpecificJob() != null ? o.getSpecificJob().getName() : "").setKey("specificJob");

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

    protected void confirmRemove(final ToothOption item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("toothOption")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "opțiunea " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final ToothOptionSpecification criteria = new ToothOptionSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(ToothOptionEditPage.class);
    }

    protected void edit(final ToothOption item) {
        UI.getCurrent().navigate(ToothOptionEditPage.class, item.getId());
    }

    private void remove(final ToothOption item) {
        try {
            dataService.deleteEntity(item.getId(), ToothOption.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting tooth option: {}", item.getId());
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
