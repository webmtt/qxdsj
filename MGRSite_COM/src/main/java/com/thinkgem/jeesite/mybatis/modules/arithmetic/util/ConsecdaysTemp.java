package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class ConsecdaysTemp {

    public static void main(String[] args){

        ConsecdaysTemp.getConsecdaysTemp();

    }

    public static Object getConsecdaysTemp() {

        List date = new ArrayList();
        Scanner input = new Scanner(System.in);
        System.out.println("请输入起始日期，格式：yyyyMMdd");
        Long dbtime2 = Long.valueOf(input.nextLine()); //第一个日期
        date.add(dbtime2);
        System.out.println("请输入终止日期，格式：yyyyMMdd");
        Long dbtime1 = Long.valueOf(input.nextLine()); //第二个日期
        date.add(dbtime1);
        System.out.println(date);

        HashMap<String, Object> params = new HashMap<String, Object>();
        String result = HttpClientUtil.get("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);
        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);

        JSONArray ja = jsonObj.getJSONArray("data");
        JSONArray array1 = new JSONArray();
        JSONArray index = new JSONArray();
        String time;
        int n=0,m=1;
        for (int i = 0; i < ja.size(); i++) {
            if(ja.getJSONObject(i).getDouble("highest_temperature")>(-1.0)) {
                index.add(i);
            }
        }System.out.println(index);

        for (int j = 0;j < index.size();j++){
            time =ja.getJSONObject(index.getInt(j)).getString("information_time");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date aa = dateFormat.parse(time);
                array1.add(aa);
            }catch (ParseException e){
                e.printStackTrace();
            }

            System.out.println(time);
        }System.out.println(array1);



       /* try {
    List<Long> longList = array1.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            System.out.println(longList);
        }catch (NumberFormatException e){
            e.fillInStackTrace();
        }*/

        /*for (int t = 0;t <num.length;t++){
            if(num[t]<date.get(0)){

            }
        }*/




       /* List<Integer> c = new LinkedList<Integer>(); // 连续的子数组

        c.add(index.getInt(0));

        for (int j = 0; j < index.size() - 1; ++j) {
            if (index.getInt(j)+ 1 == ns[i+1]) {
                c.add(ns[i+1]);
            } else {
                if (c.size() > 1) {
                    System.out.println(c);
                }

                c.clear();
                c.add(ns[i+1]);
            }
        }

        if (c.size() > 1) {
            System.out.println(c);
        }
    }
        }*/


        return params;
    }
}
