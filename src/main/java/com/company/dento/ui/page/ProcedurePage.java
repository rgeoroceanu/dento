package com.company.dento.ui.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.ProcedureLayout;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button.ClickListener;

@Component
@UIScope
@SpringView
public class ProcedurePage extends Page {
	
	private static final long serialVersionUID = 1L;
	private static final Map<String, ClickListener> navigationItems;
    static
    {
    	navigationItems = new HashMap<String, ClickListener>();
    	navigationItems.put("procedures", e -> DentoUI.getCurrent().navigateToProceduresPage());
    }
	private final ProcedureLayout procedureLayout;
	
	public ProcedurePage() {
		procedureLayout = new ProcedureLayout();
		procedureLayout.setNavigationItems(navigationItems);
		this.setLayout(procedureLayout);
	}
}
