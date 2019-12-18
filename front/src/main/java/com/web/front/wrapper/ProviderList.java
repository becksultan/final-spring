package com.web.front.wrapper;

import com.web.front.entity.Provider;

import java.util.ArrayList;
import java.util.List;

public class ProviderList {
    private List<Provider> providerList;

    public ProviderList() {
        providerList = new ArrayList<Provider>();
    }

    public List<Provider> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<Provider> providerList) {
        this.providerList = providerList;
    }
}
