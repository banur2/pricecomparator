import java.io.IOException;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.print.Doc;


public class WebScrap {

    private static void site2() throws InterruptedException
    {

        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";

        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);
        webdriver.findElement(By.xpath("//button[contains(.,'Continue to plans')]")).click();
        Thread.sleep(2000);

        Document doc = Jsoup.parse(webdriver.getPageSource());

        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.vfuk-PlanCard__detailContainer");


        for(Element e: masthead) {
            //System.out.println(e.html());
            System.out.println("============" + e.text() + "====================");
        }
        Thread.sleep(5000);
        webdriver.close();
        webdriver.quit();
    }

    public static void site1() throws InterruptedException
    {
        String url = "https://www.virginmedia.com/mobile/pay-monthly/apple/iphone-xr?intcmpid=mobilehub_pos2_banner&contractDuration=24&tariffID=1209711626";


        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(5000);
//        webdriver.findElement(By.id("duration-1")).click();
//        Thread.sleep(2000);

        Document doc = Jsoup.parse(webdriver.getPageSource());
        System.out.println(webdriver.getPageSource());

       // Document doc = Jsoup.connect(url).get();
        doc.select("p.strikethrough").remove();
        String title = doc.title();
        System.out.println(title);

        //.tariff-table-prices p

        Elements masthead = doc.select("div.tariff-table-prices");


        for(Element e: masthead) {
            //System.out.println(e.html());


            System.out.println("============" + e.text()  + "====================");
        }
        Thread.sleep(5000);
        webdriver.close();
        webdriver.quit();
    }
    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
      site1();
        Thread.sleep(5000);
        site2();





    }

}
