package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class DayRange {

    public static void main(String[] args){

        DayRange.getDayRange();

    }

    public static Object getDayRange() {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入日期，格式：yyyy-MM-dd-hh-mm");
        String dbtime1 = input.nextLine();
        System.out.println("请输入表中最起始时间，格式：yyyy-MM-dd-hh-mm");
        String dbtime2 = input.nextLine();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date date1 = null;
        try {
            date1 = format.parse(dbtime1);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }

        Date date2 = null;
        try {
            date2 = format.parse(dbtime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int a = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));


        HashMap<String, Object> params = new HashMap<String, Object>();
        String result = HttpClientUtil.get("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);
        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        JSONArray ja = jsonObj.getJSONArray("data");
        JSONObject json = new JSONObject();

        json.put("站号",ja.getJSONObject(a).getInt("stationNum"));
        json.put("站名",ja.getJSONObject(a).getString("stationName"));
        json.put("观测时次",ja.getJSONObject(a).getString("Information_time"));
        json.put("最高气温",ja.getJSONObject(a).getDouble("highest_temperature"));
        json.put("最高气温出现时间",ja.getJSONObject(a).getString("Information_time"));
        json.put("最低气温",ja.getJSONObject(a).getDouble("lowest_temperature"));
        json.put("最低气温出现时间",ja.getJSONObject(a).getString("Information_time"));
        json.put("日较差",ja.getJSONObject(a).getDouble("highest_temperature")-ja.getJSONObject(a).getDouble("lowest_temperature"));

        System.out.println(json);
        return json;
    }
}
