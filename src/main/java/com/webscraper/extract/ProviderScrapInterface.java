package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProviderScrapInterface {

    String getProviderName();
    String setup() throws InterruptedException;
    boolean extract(String htmlSource);
    HashMap<MobilePlan, ArrayList<ProviderPlan>> getProviderPlanList();
}
