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

    @Override
    public String toString() {
        return "Data " + (( this.getDataInGB()==-1) ?"Unlimited":this.getDataInGB() + "") + " GB";
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        MobilePlan mp = (MobilePlan) obj;
        return this.hashCode() == mp.hashCode();
    }

    @Override
    public int hashCode(){
        //Hashcode generation based on field attributes value
        return 2*getMinutes() + 3*getDataInGB() + 4*getTextCount() + 10;
    }
}

