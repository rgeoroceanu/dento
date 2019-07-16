package com.company.dento.model.business;

public interface Price<S> {

    S getKey();
    void setKey(S key);
    float getPrice();
    void setPrice(float price);
    String getKeyName();
}
