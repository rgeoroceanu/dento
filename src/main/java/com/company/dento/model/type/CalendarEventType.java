package com.company.dento.model.type;

import com.company.dento.ui.localization.Localizer;

public enum CalendarEventType {
    JOB_DELIVERY,
    SAMPLE,
    OTHER;

    public String getTitle() {
        switch (this) {
            case JOB_DELIVERY:
                return Localizer.getLocalizedString("jobDeliveryEvent");
            case SAMPLE:
                return Localizer.getLocalizedString("sampleEvent");
            case OTHER:
                return Localizer.getLocalizedString("otherEvent");
            default:
                return "";
        }
    }
}
