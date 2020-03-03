package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.Provider;
import com.webscraper.bo.ProviderPlan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SkyExtractor extends BaseExtractor {

    public SkyExtractor(String providerName){
        super(providerName);
    }

    @Override
    public String setup() throws InterruptedException {
        super.setURL("https://www.sky.com/shop/mobile/phones/apple/apple-iphone-xr?callsandtexts=14210&data=14997&handset=15068&swap=36MSWAP24");
        return super.setup();
    }

    /**
     * Extract the info
     * @param htmlSource
     * @return
     */
    @Override
    public boolean extract(String htmlSource) {

        Document doc = Jsoup.parse(htmlSource);

        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.c-product-header");
        if(masthead.size() == 0)
            return false;

        Provider provider = new Provider();
        provider.setName(super.getProviderName());

        for(Element e: masthead) {
            //System.out.println(e.html());
/*
============1GB Unlimited Calls and Texts £6.00 per month====================
============2GB Unlimited Calls and Texts £12.00 per month====================
============10GB Unlimited Calls and Texts £10.00 per month====================
============15GB Unlimited Calls and Texts £18.00 per month====================
============25GB Unlimited Calls and Texts £25.00 per month====================
 */
            MobilePlan plan = new MobilePlan();
            plan.setMobileModel("iPhone XR 64 GB");
            String planStr = e.text();
            //Split the data and parse
            String[] planStrParsed = planStr.split(" ");

            List<String> al = Arrays.asList(planStrParsed);
            System.out.println((al));
            ProviderPlan providerPlan = new ProviderPlan();
            providerPlan.setProvider(provider);

            //Avoid the SIM Free plan
            if(al.get(0).equals("SIM"))
                continue;
            //Extract by position - might need to do better
            plan.setDataInGB(Integer.parseInt((al.get(0).replace("GB", "")).replace("Unlimited", "-1")));
            plan.setTextCount(Integer.parseInt(al.get(1).replace("Unlimited", "-1")));
            plan.setMinutes(Integer.parseInt(al.get(1).replace("Unlimited", "-1")));

            providerPlan.setMonthlyPayment(Float.parseFloat(al.get(5).replace("£", "")));
            addProviderPlan(plan,providerPlan);


            System.out.println("============" + e.text()  + "====================");
        }

        return true;
    }



    public static void main(String[] arg) throws InterruptedException{
        WebDriverManager.chromedriver().setup();

        SkyExtractor extractor = new SkyExtractor("Sky Mobile");


        extractor.extract(extractor.setup());

        System.out.println("Provider List " + extractor.getProviderPlanList());


    }
}
