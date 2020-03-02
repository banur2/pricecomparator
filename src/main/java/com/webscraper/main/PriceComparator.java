package com.webscraper.main;

import com.webscraper.bo.MobilePlan;
import com.webscraper.bo.Provider;
import com.webscraper.bo.ProviderPlan;
import com.webscraper.extract.ProviderScrapInterface;
import com.webscraper.extract.SkyExtractor;
import com.webscraper.extract.VirginmediaExtractor;
import com.webscraper.extract.VodafoneExtractor;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class PriceComparator {
    private static PriceComparator _instance = new PriceComparator();
    private ArrayList<ProviderScrapInterface> providerList = null;

    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = null;

    //Singleton Implementation
    private PriceComparator(){
        planList = new HashMap<MobilePlan, ArrayList<ProviderPlan>>();
        providerList = new ArrayList<ProviderScrapInterface>();
    }

    public static PriceComparator getInstance(){
        return _instance;
    }

    public void addProviderList(ProviderScrapInterface provider){
        providerList.add(provider);
    }


    public ArrayList<ProviderScrapInterface> getProviderList() {
        return providerList;
    }

    public void generateReport() throws InterruptedException, IOException {
        WebDriverManager.chromedriver().setup();
        for(ProviderScrapInterface providerInterface : this.getProviderList()){
            providerInterface.extract(providerInterface.setup());
        }
        //Need to construct the table
        /*
        Mobile                Contract        Minutes      Texts        Data        Virgin Mobile        Vodafone UK        SKY       Tesco
        iPhone XR 64 GB Red   24 months        1000        1000             1GB        20                -                -           15
        iPhone XR 64 GB Red   24 months        1000        Unlimited        1GB        25               30                  20        20
        iPhone XR 64 GB Red   24 months        2000        Unlimited        5GB        40               45                  24        45
        */

        FileWriter fw = new FileWriter("C:\\pricecompare\\PriceComparator.csv");
        PrintWriter out = new PrintWriter(fw);

        //Heading for CSV file
        ArrayList<String> stringArrayList = new ArrayList<String>();

        stringArrayList.add("Mobile");
        stringArrayList.add("Contract");
        stringArrayList.add("Minutes");
        stringArrayList.add("Texts");
        stringArrayList.add("Data");

        for (ProviderScrapInterface inter : providerList){
            stringArrayList.add(inter.getProviderName());
        }

        writeToFileLine(stringArrayList, out);
        stringArrayList.clear();

        //Data
        for(MobilePlan mobilePlan: planList.keySet()){
            stringArrayList.add(mobilePlan.getMobileModel());
            stringArrayList.add("24 months");
            stringArrayList.add((mobilePlan.getMinutes()==-1)? "Unlimited" : mobilePlan.getMinutes() + "");
            stringArrayList.add((mobilePlan.getTextCount()==-1)? "Unlimited" : mobilePlan.getTextCount() + "");
            stringArrayList.add((mobilePlan.getDataInGB()==-1)? "Unlimited" : mobilePlan.getDataInGB() + "GB");

            writeToFileLine(stringArrayList, out);
            stringArrayList.clear();

        }

        out.flush();
        out.close();
        fw.close();

    }

    private void writeToFileLine(ArrayList<String > stringArrayList,PrintWriter out){
        for(String str: stringArrayList)
            out.print(str + ",");
        out.println();
    }

    public static void main(String[] arg) {
        try {
            PriceComparator.getInstance().addProviderList(new SkyExtractor("Sky Mobile"));
            PriceComparator.getInstance().addProviderList(new VirginmediaExtractor("Virigin Media"));
           // PriceComparator.getInstance().addProviderList(new VodafoneExtractor("Vodafone UK"));
            PriceComparator.getInstance().generateReport();

            System.out.println("Final Comparator List " + PriceComparator.getInstance().getPlanList());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public HashMap<MobilePlan, ArrayList<ProviderPlan>> getPlanList() {
        return planList;
    }

    public void addMobilePlan(MobilePlan plan)
    {
        ArrayList<ProviderPlan> planArrayList = new ArrayList<ProviderPlan>();
        getPlanList().put(plan, planArrayList);

    }

    public void addProviderPlan(MobilePlan mobilePlan, ProviderPlan providerPlan){
        if (planList.containsKey(mobilePlan))
        {
            System.out.println("Plan exists " + mobilePlan.getDataInGB());
            planList.get(mobilePlan).add(providerPlan);
        }
        else
        {
            ArrayList<ProviderPlan> planArrayList = new ArrayList<ProviderPlan>();
            planArrayList.add(providerPlan);
            planList.put(mobilePlan, planArrayList);
        }
    }
}
