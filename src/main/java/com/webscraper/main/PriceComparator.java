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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PriceComparator {
    //Singleton obj
    private static PriceComparator _instance = new PriceComparator();
    private ArrayList<ProviderScrapInterface> providerList = null;

    private HashMap<MobilePlan, ArrayList<ProviderPlan>> planList = null;

    //Singleton Implementation
    private PriceComparator(){
        //Creating the planList to hold all the plans
        planList = new HashMap<>();
        //Provider List to hold list of providers used for comparison
        providerList = new ArrayList<>();
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

    /**
     * Get the mobile plan - list of provider plan construct the comparator table
     * @throws InterruptedException
     * @throws IOException
     */
    public void generateReport() throws InterruptedException, IOException {
        WebDriverManager.chromedriver().setup();
        for(ProviderScrapInterface providerInterface : this.getProviderList()){
            providerInterface.extract(providerInterface.setup());
        }
        //Need to construct the table, Creating CSV File of values.
        /*
        Mobile                Contract        Minutes      Texts        Data        Virgin Mobile        Vodafone UK        SKY       Tesco
        iPhone XR 64 GB Red   24 months        1000        1000             1GB        20                -                -           15
        iPhone XR 64 GB Red   24 months        1000        Unlimited        1GB        25               30                  20        20
        iPhone XR 64 GB Red   24 months        2000        Unlimited        5GB        40               45                  24        45
        */

        FileWriter fw = new FileWriter("PriceComparator.csv");
        PrintWriter out = new PrintWriter(fw);

        //Heading for CSV file
        ArrayList<String> stringArrayList = new ArrayList<>();

        stringArrayList.add("Mobile");
        stringArrayList.add("Contract");
        stringArrayList.add("Minutes");
        stringArrayList.add("Texts");
        stringArrayList.add("Data");
        //Getting List of Providers used for comparison
        //Dynamically creating the Heading based on Provider Name given
        for (ProviderScrapInterface inter : providerList){
            stringArrayList.add(inter.getProviderName());
        }

        out.println (String.join(",",  stringArrayList));
        stringArrayList.clear();

        //Data
        for(MobilePlan mobilePlan: planList.keySet()){
            //Model assumed based on URL
            stringArrayList.add(mobilePlan.getMobileModel());
            //24 months
            stringArrayList.add("24 months");
            //-1 used for Unlimited.
            stringArrayList.add((mobilePlan.getMinutes()==-1)? "Unlimited" : mobilePlan.getMinutes() + "");
            stringArrayList.add((mobilePlan.getTextCount()==-1)? "Unlimited" : mobilePlan.getTextCount() + "");
            stringArrayList.add((mobilePlan.getDataInGB()==-1)? "Unlimited" : mobilePlan.getDataInGB() + "GB");


            ArrayList<ProviderPlan> providerPlanArrayList =  planList.get(mobilePlan);
            List<ProviderPlan> queryResult = null;
            //Getting List of Providers used for comparison
            //Iterating for plan match and extracting the plan cost.

            for (ProviderScrapInterface inter : providerList){
                //String comparator == will make sense here
                //Both uses provider name passed via constructor
                queryResult = providerPlanArrayList.stream().filter(key -> (key.getProvider().getName() == inter.getProviderName())).collect(Collectors.toList());
                if (queryResult.isEmpty())
                    stringArrayList.add("-");
                else {
                    StringBuilder planList = new StringBuilder();
                    for (ProviderPlan val : queryResult) {
                        planList.append(" £").append(val.getMonthlyPayment());
                    }
                    stringArrayList.add(planList.toString());
                }

            }
            out.println (String.join(",",  stringArrayList));

            //Clearing to get next row
            stringArrayList.clear();

        }

        out.flush();
        out.close();
        fw.close();

    }



    public static void main(String[] arg) {
        try {
            //Adding 3 providers - can find better way to add
            PriceComparator.getInstance().addProviderList(new SkyExtractor("Sky Mobile"));
            PriceComparator.getInstance().addProviderList(new VirginmediaExtractor("Virigin Media"));
            PriceComparator.getInstance().addProviderList(new VodafoneExtractor("Vodafone UK"));
            PriceComparator.getInstance().generateReport();

            System.out.println("Final Comparator List " + PriceComparator.getInstance().getPlanList());
            System.exit(0);
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
        ArrayList<ProviderPlan> planArrayList = new ArrayList<>();
        getPlanList().put(plan, planArrayList);

    }

    /**
     * Add Mobile plan if not already exists
     * Add provider plan for mobile plan
     * @param mobilePlan
     * @param providerPlan
     */
    public void addProviderPlan(MobilePlan mobilePlan, ProviderPlan providerPlan){
        //Search if already plan exists, if not add new plan
        //If exists add provider plan cost detail to the list already created ArrayList
        if (planList.containsKey(mobilePlan))
        {
            System.out.println("Plan exists " + mobilePlan.getDataInGB());
            planList.get(mobilePlan).add(providerPlan);
        }
        else
        {
            ArrayList<ProviderPlan> planArrayList = new ArrayList<>();
            planArrayList.add(providerPlan);
            planList.put(mobilePlan, planArrayList);
        }
    }
}
