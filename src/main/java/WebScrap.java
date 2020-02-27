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

    public static void main(String[] args) throws InterruptedException {
        //String url = "https://www.virginmedia.com/mobile/pay-monthly/apple/iphone-xr?intcmpid=mobilehub_pos2_banner&contractDuration=24&tariffID=1209711626";
        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";
        WebDriverManager.chromedriver().setup();
        WebDriver webdriver = new ChromeDriver();
        webdriver.get(url);
        Thread.sleep(2000);
       // webdriver.findElement(By.tagName("button")).click();
        webdriver.findElement(By.xpath("//button[contains(.,'Continue to plans')]")).click();
        //webdriver.findElement(By.xpath("//a[text()='Continue to plans']")).click();
        System.out.println (webdriver.findElements(By.tagName("button")).size());


//
//        Document doc = Jsoup.connect(url).get();
//        doc.select("p.strikethrough").remove();
//        String title = doc.title();
//        System.out.println(title);
//
//        //.tariff-table-prices p
//
//        Elements masthead = doc.select("div.tariff-table-prices");
//
//
//        for(Element e: masthead) {
//            //System.out.println(e.html());
//
//
//            System.out.println("============" + e.text()  + "====================");
//        }

//        String url = "https://www.vodafone.co.uk/mobile/phones/pay-monthly-contracts/apple/iphone-xr";
//        Document doc = Jsoup.connect(url).get();
//
//        String title = doc.title();
//        System.out.println(doc.html());
//
//        //.tariff-table-prices p
//
//        Elements masthead = doc.select("ul.vfuk-PlanCard__inline-list");
//
//
//        for(Element e: masthead) {
//            //System.out.println(e.html());
//
//
//            System.out.println("============" + e.text()  + "====================");
//        }



    }

}
