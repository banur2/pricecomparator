package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyExtractor implements ProviderScrapInterface {
    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = new HashMap<MobilePlan, ArrayList<ProviderPlan>>();
    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.sky.com/shop/mobile/phones/apple/apple-iphone-xr?callsandtexts=14210&data=14997&handset=15068&swap=36MSWAP24";
        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);

        return webdriver.getPageSource();

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> extract(String htmlSource) {

        Document doc = Jsoup.parse(htmlSource);

        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.c-product-header");


        for(Element e: masthead) {
            //System.out.println(e.html());
/*
============1GB Unlimited Calls and Texts £6.00 per month====================
============2GB Unlimited Calls and Texts £12.00 per month====================
============10GB Unlimited Calls and Texts £10.00 per month====================
============15GB Unlimited Calls and Texts £18.00 per month====================
============25GB Unlimited Calls and Texts £25.00 per month====================
 */

            System.out.println("============" + e.text()  + "====================");
        }

        return null;
    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> parse(){
         return planList;
    }

    public static void main(String[] arg) throws InterruptedException{
        WebDriverManager.chromedriver().setup();

        SkyExtractor extractor = new SkyExtractor();


        extractor.extract(extractor.setup());

        System.out.println("Provider List " + extractor.planList);


    }
}
