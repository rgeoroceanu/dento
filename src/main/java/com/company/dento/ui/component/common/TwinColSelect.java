package com.company.dento.ui.component.common;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.List;

public class TwinColSelect<T> extends HorizontalLayout {

    private final Grid<T> options;
    private final Grid<T> selected;
    private final List<T> optionItems = new ArrayList<>();
    private final List<T> selectedItems = new ArrayList<>();

    public TwinColSelect(final Class<T> itemClass, final String... properties) {
        options = new Grid<>(itemClass);
        selected = new Grid<>(itemClass);
    }
}
