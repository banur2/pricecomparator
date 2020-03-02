package com.webscraper.extract;

import com.google.common.base.Function;
import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.Provider;
import com.webscraper.bo.ProviderPlan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class VodafoneExtractor extends BaseExtractor {
    private WebDriver webdriver = null;

    public VodafoneExtractor(String providerName){
        super(providerName);
    }

    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";
        webdriver = new ChromeDriver();
        webdriver.get(url);
        //Thread.sleep(10000);
        waitforPageToLoad(webdriver);

        //need to adjust the waiting time...
        Thread.sleep(5000);
        //optanon-popup-bg
        webdriver.findElement(By.xpath("//button[contains(.,'Accept all cookies')]")).click();
        Thread.sleep(2000);
        webdriver.findElement(By.xpath("//button[contains(.,'Continue to plans')]")).click();
        Thread.sleep(2000);

        String source = webdriver.getPageSource();
        webdriver.close();
        webdriver.quit();
        return source;

    }

    @Override
    public boolean extract(String htmlSource) {

        Document doc = Jsoup.parse(htmlSource);

        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.vfuk-PlanCard__detailContainer");

        if(masthead.size() == 0)
            return false;

        Provider provider = new Provider();
        provider.setName("Vodafone");

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
            plan.setMobileModel("iPhone XR 64 GB");
            String planStr = e.text();
            String[] planStrParsed = planStr.split(" ");
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(planStrParsed);
            System.out.println((al));
            ProviderPlan providerPlan = new ProviderPlan();
            providerPlan.setProvider(provider);

            for(String s: al){
                if (s.contains("Data"))
                {
                    int data = Integer.parseInt(s.replace("Data", "").replace("GB", "").replace("Unlimited", "-1"));
                    plan.setDataInGB(data);
                    System.out.println ("data set "  + data);
                }
                else if (s.contains("Texts"))
                {
                    int limit = Integer.parseInt(s.replace("Unlimited", "-1").replace("Texts", ""));
                    plan.setTextCount(limit);
                    plan.setMinutes(limit);
                }
                else if(s.contains("Monthly"))
                {
                    providerPlan.setMonthlyPayment(Integer.parseInt(s.replace("Monthly", "").replace("£", "")));
                    addProviderPlan(plan,providerPlan);
                }

            }
            System.out.println("============" + e.text() + "====================");
        }

        return true;

    }



    public static void main(String[] arg) throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        int retry = 0;
        VodafoneExtractor extractor = new VodafoneExtractor("Vodafone UK");
        extractor.webdriver = new ChromeDriver();

        if ((extractor.extract(extractor.setup() )== false) && retry > 2) {
            retry++;
            extractor.extract(extractor.setup());
        }

        System.out.println("Provider List " + extractor.getProviderPlanList());


    }
}
