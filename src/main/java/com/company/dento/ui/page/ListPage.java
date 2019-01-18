package com.company.dento.ui.page;

import com.company.dento.model.business.Base;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.FilterDialog;
import com.company.dento.ui.component.common.FilterableGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import org.springframework.data.jpa.domain.Specification;

public abstract class ListPage<T extends Base, V extends Specification<T>> extends Page implements AfterNavigationObserver {

    protected final Button addButton;
    protected final Button printButton;
    protected final Button filterButton;
    protected final FilterableGrid<T, V> grid;
    protected final FilterDialog filterDialog;

    public ListPage(final Class<T> itemClass, final DataService dataService) {
        super(dataService);

        grid = new FilterableGrid<T, V>(itemClass, dataService);

        filterDialog = new FilterDialog();
        filterDialog.addApplyFilterListener(e -> refresh());

        addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple", "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        printButton = new Button();
        printButton.setIcon(new Icon(VaadinIcon.PRINT));
        printButton.addClassNames("dento-button-simple", "main-layout__content-menu-button");

        filterButton = new Button();
        filterButton.setIcon(new Icon(VaadinIcon.SEARCH));
        filterButton.addClassNames("dento-button-simple", "main-layout__content-menu-button");
        filterButton.addClickListener(e -> filterDialog.open());

        initLayout();
    }

    @Override
    public void afterNavigation(final AfterNavigationEvent event) {
        clearFilters();
        refresh();
    }

    @Override
    public void localize() {
        super.localize();
        filterDialog.localize();
    }

    protected void addEditColumn() {
        grid.addComponentColumn(item -> {
            final Icon icon = new Icon(VaadinIcon.EDIT);
            final Button edit = new Button();
            edit.setIcon(icon);
            edit.addClickListener(e -> edit(item));
            icon.addClassName("dento-grid-icon");
            edit.addClassName("dento-grid-action");
            return edit;
        }).setKey("edit").setFlexGrow(0).setWidth("50px");
    }

    protected void addRemoveColumn() {
        grid.addComponentColumn(item -> {
            final Icon icon = new Icon(VaadinIcon.TRASH);
            final Button remove = new Button();
            remove.setIcon(icon);
            remove.addClickListener(e -> confirmRemove(item));
            icon.addClassName("dento-grid-icon");
            remove.addClassName("dento-grid-action");
            return remove;
        }).setKey("remove").setFlexGrow(0).setWidth("50px");
    }

    protected abstract void add();
    protected abstract void initFilters();
    protected abstract void confirmRemove(final T item);
    protected abstract void edit(final T item);
    protected abstract void refresh();
    protected abstract void clearFilters();

    private void initLayout() {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout menuLayout = new HorizontalLayout(filterButton, addButton, printButton);
        layout.add(menuLayout, grid);
        layout.setHeight("100%");
        layout.setPadding(false);
        layout.setSpacing(false);
        this.setContent(layout);
    }

}
