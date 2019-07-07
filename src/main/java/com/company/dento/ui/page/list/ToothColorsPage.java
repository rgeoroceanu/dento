package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.ColorSpecification;
import com.company.dento.model.business.Color;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.ToothColorEditPage;
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
@Route(value = "toothColors")
@Log4j2
public class ToothColorsPage extends ListPage<Color, ColorSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public ToothColorsPage(final DataService dataService) {
	    super(Color.class, dataService, "Culori Dinți");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn("category");

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

    protected void confirmRemove(final Color item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("color")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "culoarea " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final ColorSpecification criteria = new ColorSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(ToothColorEditPage.class);
    }

    protected void edit(final Color item) {
        UI.getCurrent().navigate(ToothColorEditPage.class, item.getId());
    }

    private void remove(final Color item) {
        try {
            dataService.deleteEntity(item.getId(), Color.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting color: {}", item.getId());
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
