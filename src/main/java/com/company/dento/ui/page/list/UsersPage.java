package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.UserSpecification;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.UserEditPage;
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
import java.util.Collections;
import java.util.List;

@UIScope
@org.springframework.stereotype.Component
@Route(value = "users")
@Log4j2
public class UsersPage extends ListPage<User, UserSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final ConfirmDialog confirmDialog;

	public UsersPage(final DataService dataService) {
	    super(User.class, dataService, "Utilizatori");

        confirmDialog = new ConfirmDialog();

        grid.addColumn("username");
        grid.addColumn("firstName");
        grid.addColumn("lastName");
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

    protected void confirmRemove(final User item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("user")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                "utilizatorul " +  item.getUsername()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final UserSpecification criteria = new UserSpecification();
        grid.refresh(criteria);
    }

    @Override
    protected void clearFilters() { }

    protected void add() {
        UI.getCurrent().navigate(UserEditPage.class);
    }

    protected void edit(final User item) {
        UI.getCurrent().navigate(UserEditPage.class, item.getId());
    }

    private void remove(final User item) {
        try {
            dataService.deleteEntity(item.getId(), User.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting user: {}", item.getId());
        }
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getId()), 3000, Notification.Position.BOTTOM_CENTER);
    }

    @Override
    protected InputStream createPrintContent() {
        return null;
    }

    protected void initFilters() { }

    private Component createActiveComponent(final User item) {
        final Icon icon = new Icon(item.isActive() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isActive() ? "green": "red";
        icon.setColor(color);
        return icon;
    }
}
