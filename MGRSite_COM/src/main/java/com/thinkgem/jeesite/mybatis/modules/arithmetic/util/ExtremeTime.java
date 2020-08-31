package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;


public class ExtremeTime {
    public static void main(String[] args) {

        ExtremeTime.getExTime();

    }

    public static Object getExTime() {

        Scanner input = new Scanner(System.in);
        System.out.println("请输入起始日期，格式：yyyy-MM-dd-hh-mm");
        String dbtime2 = input.nextLine(); //第一个日期
        System.out.println("请输入终止日期，格式：yyyy-MM-dd-hh-mm");
        String dbtime1 = input.nextLine(); //第二个日期
        System.out.println("请输入表中最起始时间，格式：yyyy-MM-dd-hh-mm");
        String dbtime3 = input.nextLine();

        //算两个日期间隔多少天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date date1 = null;
        try {
            date1 = format.parse(dbtime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(dbtime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date3 = null;
        try {
            date3 = format.parse(dbtime3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int a = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
        int b = (int) ((date2.getTime() - date3.getTime()) / (1000 * 3600 * 24));
        int c = a + b;

        System.out.println("a=" + a);
        System.out.println("b=" + b);
        System.out.println("c=" + c);


        HashMap<String, Object> params = new HashMap<String, Object>();
        String result = HttpClientUtil.get("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);
        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        JSONArray ja = jsonObj.getJSONArray("data");
        int minIndex1 = b;//获取某段时间平均气温极小值时的下标
        int minIndex2 = b;//获取某段时间最高气温极小值时的下标
        int minIndex3 = b;//获取某段时间最低气温极小值时的下标
        int maxIndex1 = b;//获取某段时间平均气温极大值时的下标
        int maxIndex2 = b;//获取某段时间最高气温极大值时的下标
        int maxIndex3 = b;//获取某段时间最低气温极大值时的下标
        JSONObject json = new JSONObject();
        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(minIndex1).getDouble("average_temperature") > ja.getJSONObject(j).getDouble("average_temperature")) {
                minIndex1 = j;
            }
        }

        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(maxIndex1).getDouble("average_temperature") < ja.getJSONObject(j).getDouble("average_temperature")) {
                maxIndex1 = j;
            }
        }

        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(minIndex2).getDouble("highest_temperature") > ja.getJSONObject(j).getDouble("highest_temperature")) {
                minIndex2 = j;
            }
        }

        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(maxIndex2).getDouble("highest_temperature") < ja.getJSONObject(j).getDouble("highest_temperature")) {
                maxIndex2 = j;
            }
        }

        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(minIndex3).getDouble("lowest_temperature") > ja.getJSONObject(j).getDouble("lowest_temperature")) {
                minIndex3 = j;
            }
        }

        for (int j = b; j <c; j++) {
            if (ja.getJSONObject(maxIndex3).getDouble("lowest_temperature") < ja.getJSONObject(j).getDouble("lowest_temperature")) {
                maxIndex3 = j;
            }
        }

        json.put("该段时间平均气温极小值",ja.getJSONObject(minIndex1).getDouble("average_temperature"));
        json.put("该段时间平均气温出现极小值的时间",ja.getJSONObject(minIndex1).getString("Information_time"));
        json.put("该段时间平均气温极大值",ja.getJSONObject(maxIndex1).getDouble("average_temperature"));
        json.put("该段时间平均气温出现极大值的时间" ,ja.getJSONObject(maxIndex1).getString("Information_time"));
        json.put("该段时间最高气温极小值",ja.getJSONObject(minIndex2).getDouble("highest_temperature"));
        json.put("该段时间最高气温出现极小值的时间",ja.getJSONObject(minIndex2).getString("Information_time"));
        json.put("该段时间最高气温极大值",ja.getJSONObject(maxIndex2).getString("highest_temperature"));
        json.put("该段时间最高气温出现极大值的时间",ja.getJSONObject(minIndex2).getString("Information_time"));
        json.put("该段时间最低气温极小值" ,ja.getJSONObject(minIndex3).getString("lowest_temperature"));
        json.put("该段时间最低气温出现极小值的时间",ja.getJSONObject(minIndex3).getString("Information_time"));
        json.put("该段时间最低气温极大值",ja.getJSONObject(minIndex3).getString("lowest_temperature"));
        json.put("该段时间最低气温出现极大值的时间",ja.getJSONObject(minIndex3).getString("Information_time"));

        System.out.println(json);
        return json;

    }
}
