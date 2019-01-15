package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Collectors;

public class FilterableGrid<T extends Base, V extends Specification<T>> extends Grid<T> implements Localizable {

    private final ConfigurableFilterDataProvider<T, Void, V> filterableDataProvider;
    private final DataService dataService;
    private final Class<T> itemClass;
    private final List<Column> nonResponsiveColumns = new ArrayList<>();
    private Map<String, ValueProvider<T, ?>> valueProviders = new LinkedHashMap<>();

    public FilterableGrid(final Class<T> itemClass, final DataService dataService) {
        super(itemClass);
        this.itemClass = itemClass;
        this.dataService = dataService;
        filterableDataProvider = initDataProvider();

        initGrid();

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> handleSizeChange(e.getHeight() > e.getWidth()));
        UI.getCurrent().getPage().executeJavaScript("window.dispatchEvent(new Event('resize'));");
    }

    public void refresh(final V criteria) {
        this.setDataProvider(filterableDataProvider);
        filterableDataProvider.setFilter(criteria);
        filterableDataProvider.refreshAll();
    }

    public void setNonResponsiveColumns(final Column... columns) {
        nonResponsiveColumns.addAll(Arrays.asList(columns));
    }


    public void setItemDetailsProviders(final Map<String, ValueProvider<T, ?>> valueProviders) {
        this.valueProviders.clear();
        this.valueProviders.putAll(valueProviders);
    }

    private Component createItemDetails(final T item) {
        final FormLayout layout = new FormLayout();
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        valueProviders.forEach((field, getter) -> {
            final Label fieldName = new Label(field);
            final String value = String.format("%s", getter.apply(item));
            final Label valueLabel = new Label(value);
            layout.addFormItem(valueLabel, fieldName);
        });

        return layout;
    }

    @Override
    public void localize() {
        this.getColumns().stream()
                .filter(column -> !Arrays.asList("remove", "add", "edit", "print").contains(column.getKey()))
                .forEach(column -> column.setHeader(Localizer.getLocalizedString(column.getKey())));
    }

    private void handleSizeChange(boolean smallScreen) {
        this.getColumns().stream()
                .filter(column -> !Arrays.asList("remove", "add", "edit", "print").contains(column.getKey()))
                .forEach(column -> column.setVisible(!smallScreen));

        nonResponsiveColumns.forEach(c -> c.setVisible(true));
        this.setDetailsVisibleOnClick(smallScreen);
    }

    private void initGrid() {
        this.setDataProvider(filterableDataProvider);
        this.setSelectionMode(SelectionMode.NONE);
        this.setSizeFull();
        this.getElement().setAttribute("theme", "no-border row-stripes");
        this.getColumns().forEach(this::removeColumn);
        this.setPageSize(20);
        this.addClassName("dento-grid");
        this.setItemDetailsRenderer(new ComponentRenderer<>(this::createItemDetails));
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
