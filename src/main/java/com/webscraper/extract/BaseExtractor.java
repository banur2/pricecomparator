package com.webscraper.extract;

import com.google.common.base.Function;
import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.ProviderPlan;
import com.webscraper.main.PriceComparator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseExtractor implements ProviderScrapInterface {

    private String URL;
    private String providerName;

    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = new HashMap<>();


    /** Send provider Name as part of construction
     *  @param providerName
     */
    public BaseExtractor(String providerName){
        this.providerName = providerName;
    }

    @Override
    public String getProviderName() {
        return this.providerName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * Get Page via Selenium - better behavior than get HTML via URL
     * Overwritten if any clicks or UI action needed
     */
    @Override
    public String setup() throws InterruptedException {

        if (this.getURL().isEmpty())
            return null;
        WebDriver webdriver = new ChromeDriver();
        webdriver.get(this.getURL());
        Thread.sleep(5000);
        this.waitforPageToLoad(webdriver);

        String source = webdriver.getPageSource();
        webdriver.close();
        webdriver.quit();
        return source;

    }

    @Override
    public HashMap<MobilePlan, ArrayList<ProviderPlan>> getProviderPlanList(){
        return planList;
    }

    /**
     * Check javascript state of the HTML and wait
     * @param webdriver
     */
    protected void waitforPageToLoad(WebDriver webdriver){
        Wait<WebDriver> wait = new WebDriverWait(webdriver, Duration.ofSeconds(30));
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                System.out.println("Current Window State       : "
                        + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
    }

    /**
     * Add Provider plan
     * Add to Individual provider plan List
     * Add to Global Plan list for comparator
     * @param mobilePlan
     * @param providerPlan
     */
    protected void addProviderPlan(MobilePlan mobilePlan, ProviderPlan providerPlan){
        if (this.getProviderPlanList().containsKey(mobilePlan))
        {
            System.out.println("Plan exists " + mobilePlan.getDataInGB());
            this.getProviderPlanList().get(mobilePlan).add(providerPlan);
        }
        else
        {
            ArrayList<ProviderPlan> planArrayList = new ArrayList<>();
            planArrayList.add(providerPlan);
            this.getProviderPlanList().put(mobilePlan, planArrayList);
        }
        PriceComparator instanceComparator = PriceComparator.getInstance();
        instanceComparator.addProviderPlan(mobilePlan, providerPlan);
    }
}
