package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.Material;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.JobTemplateEditPage;
import com.company.dento.ui.page.edit.MaterialEditPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jni.Local;
import org.springframework.security.access.annotation.Secured;

import java.io.InputStream;

@UIScope
@org.springframework.stereotype.Component
@Secured(value = {"USER", "ADMIN"})
@Route(value = "materials")
@Log4j2
public class MaterialsPage extends ListPage<Material, MaterialSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public MaterialsPage(final DataService dataService) {
	    super(Material.class, dataService);

        confirmDialog = new ConfirmDialog();

        grid.addColumn("name");
        grid.addColumn(m -> Localizer.getLocalizedString(m.getMeasurementUnit().name())).setKey("measurementUnit");
        grid.addColumn("pricePerUnit");

        grid.addComponentColumn(this::createPerJobComponent).setKey("perJob");
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

    protected void confirmRemove(final Material item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("material")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "materialul " +  item.getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final MaterialSpecification criteria = new MaterialSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(MaterialEditPage.class);
    }

    protected void edit(final Material item) {
        UI.getCurrent().navigate(MaterialEditPage.class, item.getId());
    }

    private Component createActiveComponent(final Material item) {
        final Icon icon = new Icon(item.isActive() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isActive() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private Component createPerJobComponent(final Material item) {
        return new Label(item.isPerJob() ? "pe lucrare" : "pe dinte");
    }

    private void remove(final Material item) {
        try {
            dataService.deleteEntity(item.getId(), JobTemplate.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting material: {}", item.getId());
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
