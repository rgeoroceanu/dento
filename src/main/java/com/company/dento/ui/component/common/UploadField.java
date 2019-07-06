package com.company.dento.ui.component.common;

import com.company.dento.model.business.StoredFile;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import elemental.json.Json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UploadField extends AbstractCompositeField<VerticalLayout, UploadField, Set<StoredFile>> {

    private static final int MAX_FILE_SIZE = 1024*1024*2;
    private final Set<StoredFile> value = new HashSet<>();
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Div valuesList = new Div();

    public UploadField() {
        super(null);

        upload.setAutoUpload(true);
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFiles(5);
        upload.setMaxFileSize(MAX_FILE_SIZE);

        final Button button = new Button();
        button.addClassName("dento-button-simple");
        button.setIcon(new Icon(VaadinIcon.UPLOAD));
        button.setText("");

        upload.setUploadButton(button);
        upload.setWidth("100%");
        upload.addSucceededListener(this::processUpload);

        this.getContent().add(upload, valuesList);
        this.getContent().setPadding(false);
        this.getContent().setSpacing(false);
        this.getContent().addClassName("dento-form-field");
    }

    public void setMaxFiles(final int maxFiles) {
        upload.setMaxFiles(maxFiles);
    }

    @Override
    protected void setPresentationValue(final Set<StoredFile> storedFiles) {
        value.clear();
        value.addAll(storedFiles);
        valuesList.removeAll();
        storedFiles.forEach(this::addFileDisplayButton);
    }

    private void processUpload(SucceededEvent event) {
        final StoredFile file = new StoredFile();
        file.setName(event.getFileName());

        try {
            file.setContent(buffer.getInputStream(event.getFileName()).readAllBytes());
        } catch (IOException e) {
            return;
        }

        addFileDisplayButton(file);
        value.add(file);
        setModelValue(value, true);
        hideFileProgress(file.getName());
    }

    private void addFileDisplayButton(final StoredFile storedFile) {
        final HorizontalLayout hl = new HorizontalLayout();

        final Button display = new Button();
        display.setText(storedFile.getName());
        display.addClassName("upload-display-button");
        display.addClickListener(e -> this.showImage(storedFile));

        final Button remove = new Button();
        remove.setIcon(new Icon(VaadinIcon.CLOSE));
        remove.addClickListener(e -> this.removeFile(storedFile, hl));
        remove.addClassName("upload-remove-button");

        hl.add(display, remove);
        hl.setWidth("100%");
        valuesList.add(hl);
    }

    private void removeFile(final StoredFile storedFile, final HorizontalLayout displayLayout) {
        value.remove(storedFile);
        valuesList.remove(displayLayout);
        setModelValue(value, true);
    }

    private void hideFileProgress(final String filename) {
        upload.getElement().setPropertyJson("files", Json.createArray());
    }

    private void showImage(final StoredFile file) {
        final Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        final StreamResource sr = new StreamResource("stream",
                (InputStreamFactory) () -> new ByteArrayInputStream(file.getContent()));

        final Image image = new Image();
        image.setHeight("768px");
        image.setSrc(sr);

        dialog.add(image);
        dialog.open();
    }
}
