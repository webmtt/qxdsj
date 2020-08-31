package com.thinkgem.jeesite.mybatis.modules.arithmetic.util; /**
 * 春季回暖统计算法
 */

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class springwarmAlogorithm {
    public static void main(String[] args) {
        JSONObject finaljson = springthaw();
        System.out.println(finaljson);
    }

    public static JSONObject springthaw() {

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

        HashMap<String, Object> params = new HashMap<String, Object>();
        String string = HttpClientUtil.post("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);//获取json数据
        System.out.println(string);
        JSONObject jsonObj = JSONObject.fromObject(string);
        JSONArray data = jsonObj.getJSONArray("data");//得到json中的data数据
        System.out.println(data);
        int k = 0, index = 0;
        List avertem = new ArrayList();
        for (int i = b; i < c; i++) {          //遍历数组
            JSONObject jo = data.getJSONObject(i);
            Double averagetemperature = jo.getDouble("average_temperature");
            avertem.add(averagetemperature);        //将平均气温数据存到一个list中
        }
        System.out.println(avertem);
        List sublist = new ArrayList();
        for (k = 0; k < avertem.size(); k++) {
            double tem = (double) avertem.get(k);
            if (tem >= 3) {
                sublist.add(k);
                index++;
            } else {
                if(index>=3){
                    System.out.println(sublist);
                    break;
                }
                index = 0;
                sublist.clear();
            }
        }
        JSONObject first = data.getJSONObject((Integer) sublist.get(0) + b);
        System.out.println(first);
        JSONObject last = data.getJSONObject((Integer) sublist.get(sublist.size() - 1) + b);
        System.out.println(last);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("站名",first.get("stationName"));
        jsonObject1.put("站号",first.get("stationNum"));
        jsonObject1.put("回暖初日",first.get("Information_time"));
        jsonObject1.put("回暖终日",last.get("Information_time"));
        jsonObject1.put("天数",sublist.size());
        return jsonObject1;
    }
}


