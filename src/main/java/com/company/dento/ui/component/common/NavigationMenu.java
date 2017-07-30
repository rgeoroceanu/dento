package com.company.dento.ui.component.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.company.dento.ui.localization.Localizable;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class NavigationMenu extends CustomComponent implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private VerticalLayout layout;
	private Map<Button, String> captionIds = new HashMap<>();
	
	public NavigationMenu() {
		layout = new VerticalLayout();
		this.addStyleName("navigation-bar");
		this.setCompositionRoot(layout);
		this.setHeight(100, Unit.PERCENTAGE);
	}
	
	public void addNavigationButton(String captionId, VaadinIcons icon, ClickListener listener) {
		Button button = createNavigationButton(listener);
		button.setIcon(icon);
		layout.addComponent(button);
		captionIds.put(button, captionId);
	}
	
	private Button createNavigationButton(ClickListener clickListener) {
		Button button = new Button();
		button.addStyleName("button");
		button.addClickListener(clickListener);
		return button;
	}
	
	@Override
	public void localize() {
		for (Entry<Button, String> e : captionIds.entrySet()) {
			e.getKey().setCaption(e.getValue());
		}
	}
	
}
