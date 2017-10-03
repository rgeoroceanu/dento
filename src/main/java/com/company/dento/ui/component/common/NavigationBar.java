package com.company.dento.ui.component.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NavigationBar extends CustomComponent implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private HorizontalLayout layout;
	private Map<Button, String> captionIds = new HashMap<>();
	
	public NavigationBar() {
		layout = new HorizontalLayout();
		layout.setSpacing(false);
		addHomeButton();
		this.setCompositionRoot(layout);
	}
	
	public void addNavigationButton(String captionId, ClickListener listener) {
		Button button = createNavigationButton(listener);
		
		if (layout.getComponentCount() != 0) {
			Button delimiter = createDelimiter();
			layout.addComponent(delimiter);
		}
		
		layout.addComponent(button);
		captionIds.put(button, captionId);
		localize();
	}
	
	public void clear() {
		layout.removeAllComponents();
		addHomeButton();
	}
	
	private void addHomeButton() {
		Button button = createNavigationButton(e -> DentoUI.getCurrent().navigateToStartPage());
		button.setIcon(VaadinIcons.HOME);
		layout.addComponent(button);
	}
	
	private Button createNavigationButton(ClickListener clickListener) {
		Button button = new Button();
		button.addStyleName(ValoTheme.BUTTON_LINK);
		button.addClickListener(clickListener);
		return button;
	}
	
	private Button createDelimiter() {
		Button button = new Button();
		button.addStyleName(ValoTheme.BUTTON_LINK);
		button.setCaption("/");
		return button;
	}

	@Override
	public void localize() {
		for (Entry<Button, String> e : captionIds.entrySet()) {
			e.getKey().setCaption(Localizer.getLocalizedString(e.getValue()));
		}
	}
	
}
