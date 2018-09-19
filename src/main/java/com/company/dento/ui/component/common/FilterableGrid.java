package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.LocalDateRenderer;

import java.util.Map;
import java.util.stream.Collectors;

public class FilterableGrid<T extends Base, V> extends Grid<T> implements Localizable {

    private final ConfigurableFilterDataProvider<T, Void, V> filterableDataProvider;
    private final DataService dataService;
    private final Class<T> itemClass;

    public FilterableGrid(final Class<T> itemClass, final DataService dataService) {
        super(itemClass);
        this.itemClass = itemClass;
        this.dataService = dataService;
        this.filterableDataProvider = initDataProvider();
        this.setDataProvider(filterableDataProvider);
        initGrid();
    }

    public void refresh(final V criteria) {
        filterableDataProvider.setFilter(criteria);
        filterableDataProvider.refreshAll();
    }

    @Override
    public void localize() {
        this.getColumns().forEach(column ->
                column.setHeader(Localizer.getLocalizedString(column.getKey())));
    }

    private void initGrid() {
        this.setSelectionMode(SelectionMode.NONE);
        this.setSizeFull();
        this.getElement().setAttribute("theme", "no-border row-stripes");
        removeDefaultColumns();
        this.addColumn(new LocalDateRenderer<>(item -> item.getCreated().toLocalDate(), "d.M.yyyy"))
                .setKey("formattedDate").setWidth("250px");
    }

    private void removeDefaultColumns() {
        this.removeColumnByKey("id");
        this.removeColumnByKey("created");
        this.removeColumnByKey("updated");
        this.removeColumnByKey("version");
    }

    private ConfigurableFilterDataProvider<T, Void, V> initDataProvider() {
        final CallbackDataProvider<T, V> dataProvider = DataProvider.fromFilteringCallbacks(query -> {
                    Map<String, Boolean> sortOrder = query.getSortOrders().stream()
                            .collect(Collectors.toMap(SortOrder::getSorted,
                                    sort -> sort.getDirection() == SortDirection.ASCENDING));

                    final V filter = query.getFilter().orElse(null);

                    return dataService.getByCriteria(itemClass, filter, query.getOffset(), query.getLimit(), sortOrder).stream();
                },
                query -> dataService.countByCriteria(itemClass, query.getFilter().orElse(null)));

        return dataProvider.withConfigurableFilter();
    }
}
