package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;

public class VirginmediaExtractor implements ProviderScrapInterface {
    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = new HashMap<MobilePlan, ArrayList<ProviderPlan>>();
    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.virginmedia.com/mobile/pay-monthly/apple/iphone-xr?intcmpid=mobilehub_pos2_banner&contractDuration=24&tariffID=1209711626";


        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);

        return webdriver.getPageSource();

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> extract(String htmlSource) {

        Document doc = Jsoup.parse(htmlSource);
        doc.select("p.strikethrough").remove();
        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.tariff-table-prices");


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

            System.out.println("============" + e.text()  + "====================");
        }

        return null;
    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> parse(){
         return planList;
    }
}
