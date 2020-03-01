package com.webscraper.extract;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;

public class VodafoneExtractor implements ProviderScrapInterface {
    @Override
    public String setup() throws InterruptedException {
        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";

        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);
        webdriver.findElement(By.xpath("//button[contains(.,'Continue to plans')]")).click();
        Thread.sleep(2000);

        return null;

    }

    @Override
    public void extract(String htmlSource) {

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> parse(){
         return null;
    }
}
