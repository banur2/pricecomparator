package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.Provider;
import com.webscraper.bo.ProviderPlan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VirginmediaExtractor extends BaseExtractor {

    public VirginmediaExtractor(String providerName){
        super(providerName);
    }
     @Override
    public String setup() throws InterruptedException {
        super.setURL("https://www.virginmedia.com/mobile/pay-monthly/apple/iphone-xr?intcmpid=mobilehub_pos2_banner&contractDuration=24&tariffID=1209711626");
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
        doc.select("p.strikethrough").remove();
        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.tariff-table-prices");

        if(masthead.size() == 0)
            return false;

        Provider provider = new Provider();
        provider.setName(super.getProviderName());
        for(Element e: masthead) {
            //System.out.println(e.html());
/*
============£33 1000 Unlimited 1gb====================
============£36 1500 Unlimited 3gb====================
============£37 5000 Unlimited 8gb====================
============£41 5000 Unlimited 36gb 3x data====================
============£47 5000 Unlimited 90gb 3x data====================
============£49 5000 Unlimited 120gb 3x data====================
============£35 1000 Unlimited 2gb====================
============£40 5000 Unlimited 10gb====================
============£42 5000 Unlimited 20gb====================
============£45 5000 Unlimited 25gb====================
============£52 5000 Unlimited 50gb====================
============£57 5000 Unlimited 100gb====================
============£43 Unlimited Unlimited Unlimited====================
 */
            MobilePlan plan = new MobilePlan();
            plan.setMobileModel("iPhone XR 64 GB");
            String planStr = e.text();
            String[] planStrParsed = planStr.split(" ");

            List<String> al = Arrays.asList(planStrParsed);
            System.out.println((al));
            ProviderPlan providerPlan = new ProviderPlan();
            providerPlan.setProvider(provider);

            //parsed by position
            plan.setDataInGB(Integer.parseInt((al.get(3).replace("gb", "")).replace("Unlimited", "-1")));
            plan.setTextCount(Integer.parseInt(al.get(2).replace("Unlimited", "-1")));
            plan.setMinutes(Integer.parseInt(al.get(1).replace("Unlimited", "-1")));

            providerPlan.setMonthlyPayment(Float.parseFloat(al.get(0).replace("£", "")));
            addProviderPlan(plan,providerPlan);

            System.out.println("============" + e.text()  + "====================");
        }

        return true;
    }

    public static void main(String[] arg) throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        VirginmediaExtractor extractor = new VirginmediaExtractor("Virigin Media");
        extractor.extract(extractor.setup());
        System.out.println("Provider List " + extractor.getProviderPlanList());
    }

}
