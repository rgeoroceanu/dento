package com.company.dento.model.business;

public interface SoftDelete {

    boolean isDeleted();
    void setDeleted(boolean deleted);
    boolean isActive();
    void setActive(boolean deleted);
}
