package com.webscraper.bo;

public class MobilePlan {
    private String mobileModel;
    private int contractMonths;
    //-1 for Unlimited
    private int minutes;
    //-1 for Unlimited
    private int textCount;
    //-1 for Unlimited
    private int dataInGB;

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public int getContractMonths() {
        return contractMonths;
    }

    public void setContractMonths(int contractMonths) {
        this.contractMonths = contractMonths;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }

    public int getDataInGB() {
        return dataInGB;
    }

    public void setDataInGB(int dataInGB) {
        this.dataInGB = dataInGB;
    }
}
