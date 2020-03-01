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

public class VodafoneExtractor implements ProviderScrapInterface {
    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = new HashMap<MobilePlan, ArrayList<ProviderPlan>>();
    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";

        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);
        webdriver.findElement(By.xpath("//button[contains(.,'Continue to plans')]")).click();
        Thread.sleep(2000);

        return webdriver.getPageSource();

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> extract(String htmlSource) {

        Document doc = Jsoup.parse(htmlSource);

        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.vfuk-PlanCard__detailContainer");


        for(Element e: masthead) {
            //System.out.println(e.html());
            /*
            iPhone XR deals and contracts | Vodafone
============Data6GB Minutes & TextsUnlimited Upfront£29 Monthly£46====================
============Data6GB Minutes & TextsUnlimited Upfront£29 Monthly£53====================
============Data24GB Minutes & TextsUnlimited Upfront£29 Monthly£51====================
============Data24GB Minutes & TextsUnlimited Upfront£29 Monthly£58====================
============DataUnlimited Minutes & TextsUnlimited Upfront£29 Monthly£54====================
============DataUnlimited Minutes & TextsUnlimited Upfront£29 Monthly£58====================
============DataUnlimited Minutes & TextsUnlimited Upfront£29 Monthly£65====================
============DataUnlimited Minutes & TextsUnlimited Upfront£29 Monthly£63====================
============DataUnlimited Minutes & TextsUnlimited Upfront£29 Monthly£70====================
============Data2GB Minutes & TextsUnlimited Upfront£119 Monthly£42====================
             */
            MobilePlan plan = new MobilePlan();
            String planStr = e.text();
            String[] planStrParsed = planStr.split(" ");
          //  System.out.println((planStrParsed));
            System.out.println("============" + e.text() + "====================");
        }

        return null;

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> parse(){
         return null;
    }

    public static void main(String[] arg) throws InterruptedException{
        VodafoneExtractor extractor = new VodafoneExtractor();

        extractor.extract(extractor.setup());



    }
}
