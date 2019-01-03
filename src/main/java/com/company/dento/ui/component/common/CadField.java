package com.company.dento.ui.component.common;

import com.company.dento.model.business.CadFile;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;

import java.util.List;

public class CadField extends AbstractCompositeField<VerticalLayout, CadField, List<CadFile>> {

    private final Upload upload = new Upload();

    public CadField() {
        super(null);
        upload.setAutoUpload(true);
        upload.setDropAllowed(false);

        final Button button = new Button();
        button.addClassName("dento-button-simple");
        button.setIcon(new Icon(VaadinIcon.UPLOAD));
        button.setText("");

        upload.setUploadButton(button);
        
    }

    @Override
    protected void setPresentationValue(final List<CadFile> cadFiles) {

    }

    private void addImage() {
        final Anchor anchor = new Anchor();
       // anchor.add
    }
}
