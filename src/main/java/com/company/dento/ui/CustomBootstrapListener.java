package com.company.dento.ui;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CustomBootstrapListener implements BootstrapListener {

    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();

        Element head = document.head();

        head.appendChild(createMeta(document, "viewport", "width=device-width"));
        head.appendChild(createMeta(document, "mobile-web-app-capable", "yes"));
        head.appendChild(createMeta(document, "apple-mobile-web-app-capable", "yes"));
    }

    private Element createMeta(Document document, String property, String content) {
        Element meta = document.createElement("meta");
        meta.attr("property", property);
        meta.attr("content", content);
        return meta;
    }
}