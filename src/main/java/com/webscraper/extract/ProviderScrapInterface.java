package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProviderScrapInterface {

    String setup() throws InterruptedException;
    void extract(String htmlSource);
    HashMap<MobilePlan, ArrayList<ProviderPlan>> parse();
}
