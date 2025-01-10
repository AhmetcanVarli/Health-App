package com.example.bitirme_proje.Utulities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String,List<String>> getData(){

        HashMap<String, List<String>> expandaleListDetail = new HashMap<String,List<String>>();

        List<String> time09 = new ArrayList<String>();
        time09.add("09:00");
        time09.add("09:10");
        time09.add("09:20");
        time09.add("09:30");
        time09.add("09:40");
        time09.add("09:50");

        List<String> time10 = new ArrayList<String>();
        time10.add("10:00");
        time10.add("10:10");
        time10.add("10:20");
        time10.add("10:30");
        time10.add("10:40");
        time10.add("10:50");

        List<String> time11 = new ArrayList<String>();
        time11.add("11:00");
        time11.add("11:10");
        time11.add("11:20");
        time11.add("11:30");
        time11.add("11:40");
        time11.add("11:50");

        List<String> time12 = new ArrayList<String>();
        time12.add("12:00");
        time12.add("12:10");
        time12.add("12:20");
        time12.add("12:30");

       List<String> time13 = new ArrayList<String>();
        time13.add("13:30");
        time13.add("13:40");
        time13.add("13:50");

        List<String> time14 = new ArrayList<String>();
        time14.add("14:00");
        time14.add("14:10");
        time14.add("14:20");
        time14.add("14:30");
        time14.add("14:40");
        time14.add("14:50");

        List<String> time15 = new ArrayList<String>();
        time15.add("15:00");
        time15.add("15:10");
        time15.add("15:20");
        time15.add("15:30");
        time15.add("15:40");
        time15.add("15:50");





        expandaleListDetail.put("09:00",time09);
        expandaleListDetail.put("10:00",time10);
        expandaleListDetail.put("11:00",time11);
        expandaleListDetail.put("12:00",time12);
        expandaleListDetail.put("13:00",time13);
        expandaleListDetail.put("14:00",time14);
        expandaleListDetail.put("15:00",time15);

        return expandaleListDetail;

    }


}
