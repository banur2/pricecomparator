package com.webscraper.bo;

import com.webscraper.bo.Provider;

public class ProviderPlan {

    private float monthlyPayment;
    //Not used now
    private float upfrontCost;
    private Provider provider;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public float getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(float monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public float getUpfrontCost() {
        return upfrontCost;
    }

    public void setUpfrontCost(float upfrontCost) {
        this.upfrontCost = upfrontCost;
    }

    @Override
    public String toString() {
        return this.getProvider().getName() + " £" + monthlyPayment ;
    }
}
