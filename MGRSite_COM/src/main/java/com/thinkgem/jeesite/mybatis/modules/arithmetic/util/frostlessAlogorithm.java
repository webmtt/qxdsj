package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;
/**
 * 无霜期统计算法
 */

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class frostlessAlogorithm {
    public static void main(String[] args) {

        JSONObject finaljson = frostless();
        System.out.println(finaljson);
    }

    public static JSONObject frostless() {

        /*Scanner input = new Scanner(System.in);
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
        int c = a + b;*/


        String kq_time = "2020-01-31T16:00:00.000+0000";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ");
        try {
            Date date = df.parse(kq_time);
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            kq_time = df2.format(date);
            System.out.println(kq_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        String json = HttpClientUtil.post("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);//获取json数据
        System.out.println(json);
        JSONObject jsonObj = JSONObject.fromObject(json);
        JSONArray data = jsonObj.getJSONArray("data");//得到json中的data数据
        System.out.println(data);
        int k = 0, index = 0;
        List<List<Integer>> finallist = new ArrayList<List<Integer>>();
        JSONObject jsonObject1 = new JSONObject();
        List lowesttem = new ArrayList();
        for (int i = 0; i < data.size(); i++) {          //遍历数组
            JSONObject jo = data.getJSONObject(i);
            Double lowesttemperature = jo.getDouble("lowest_temperature");
            if(lowesttemperature >= 2){
                lowesttem.add(jo);//将平均气温数据存到一个list中
            }
        }
        System.out.println(lowesttem);
        Map<String,Object> map = new HashMap<String, Object>();
        for(int m =0; m< lowesttem.size(); m++){
            JSONObject objson = JSONObject.fromObject(lowesttem.get(m));
            map.put(String.valueOf(objson.get("information_time")),lowesttem.get(m));
        }
        System.out.println(map);
        int begin = 0, end = 0;
        return jsonObject1;
    }
}
