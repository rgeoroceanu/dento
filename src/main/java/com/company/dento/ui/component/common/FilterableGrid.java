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
    private final Map<Column<T>, ValueProvider<T, ?>> valueProviders;

    public FilterableGrid(final Class<T> itemClass, final DataService dataService) {
        super(itemClass);
        this.itemClass = itemClass;
        this.dataService = dataService;
        filterableDataProvider = initDataProvider();
        valueProviders = new LinkedHashMap<>();

        initGrid();

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> handleSizeChange(e.getHeight() > e.getWidth()));
        UI.getCurrent().getPage().executeJavaScript("window.dispatchEvent(new Event('resize'));");
        this.addItemClickListener(e -> this.getElement().executeJavaScript("requestAnimationFrame((function() { this.notifyResize(); }).bind(this))"));
    }

    @Override
    public Column<T> addColumn(final ValueProvider<T, ?> valueProvider) {
         final Column<T> column = super.addColumn(valueProvider);
         if (valueProviders != null && column != null) {
             valueProviders.put(column, valueProvider);
         }
         return column;
    }

    @Override
    public void removeColumn(Column<T> column) {
        super.removeColumn(column);
        if (valueProviders != null) {
            valueProviders.remove(column);
        }
    }

    public void refresh(final V criteria) {
        this.setDataProvider(filterableDataProvider);
        filterableDataProvider.setFilter(criteria);
        filterableDataProvider.refreshAll();
        this.getElement().executeJavaScript("this.notifyResize()");
    }

    public void setNonResponsiveColumns(final Column... columns) {
        nonResponsiveColumns.addAll(Arrays.asList(columns));
    }

    private Component createItemDetails(final T item) {
        final FormLayout layout = new FormLayout();
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        valueProviders.forEach((column, getter) -> {
            if (nonResponsiveColumns.contains(column)) {
                return;
            }
            final Label fieldName = new Label(Localizer.getLocalizedString(column.getKey()));
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
        this.setItemDetailsRenderer(new ComponentRenderer<>(this::createItemDetails));
    }

    private void handleSizeChange(boolean smallScreen) {
        this.getColumns().stream()
                .filter(column -> !Arrays.asList("remove", "add", "edit", "print").contains(column.getKey()))
                .forEach(column -> column.setVisible(!smallScreen));

        nonResponsiveColumns.forEach(c -> {
            c.setVisible(true);
            final String width = smallScreen ? "50px" : "unset";
            c.setWidth(width);
        });

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
