package com.company.dento.model.business;

public interface Price<S> {

    S getKey();
    void setKey(S key);
    int getPrice();
    void setPrice(int price);
    String getKeyName();
}
