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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class VodafoneExtractor implements ProviderScrapInterface {
    private WebDriver webdriver = null;
    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = new HashMap<MobilePlan, ArrayList<ProviderPlan>>();
    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";


        webdriver.get(url);
        //Thread.sleep(10000);
        Wait<WebDriver> wait = new WebDriverWait(webdriver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                System.out.println("Current Window State       : "
                        + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });

        //need to adjust the waiting time...
        Thread.sleep(2000);
        //optanon-popup-bg
        webdriver.findElement(By.xpath("//button[contains(.,'Accept all cookies')]")).click();
        Thread.sleep(2000);
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
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(planStrParsed);
            System.out.println((al));
            ProviderPlan providerPlan = new ProviderPlan();

            for(String s: al){
                if (s.contains("Data"))
                {
                    plan.setDataInGB(Integer.parseInt(s.replace("Data", "").replace("GB", "").replace("Unlimited", "-1")));
                }
                else if (s.contains("Texts"))
                {
                    int limit = Integer.parseInt(s.replace("Unlimited", "-1").replace("Texts", ""));
                    plan.setDataInGB(limit);
                    plan.setMinutes(limit);
                }
                else if(s.contains("Monthly"))
                {

                    providerPlan.setMonthlyPayment(Integer.parseInt(s.replace("Monthly", "").replace("£", "")));

                }
                ArrayList<ProviderPlan> planArrayList = new ArrayList<ProviderPlan>();
                planArrayList.add(providerPlan);
                /*
                Now it stores all mobile plan and provider cost plan not cumulative.. need to write code to check and do composite..
                Can you give me some good ideas - Banu
                my idea for a mobile there can be multiple vendor plans

                 */
                planList.put(plan, planArrayList);

            }
            System.out.println("============" + e.text() + "====================");
        }

        return planList;

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> parse(){
         return planList;
    }

    public static void main(String[] arg) throws InterruptedException{
        WebDriverManager.chromedriver().setup();

        VodafoneExtractor extractor = new VodafoneExtractor();
        extractor.webdriver = new ChromeDriver();

        extractor.extract(extractor.setup());

        System.out.println("Provider List " + extractor.planList);


    }
}
