package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class coldwaveAlogorithm {

    public static void main(String[] args) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String josn = HttpClientUtil.post("http://121.36.14.224:8085/temperaturedata/selectStaTemp", params);//获取json数据
        System.out.println(josn);
        JSONObject finaljson = coldwava(josn);
        System.out.println(finaljson);
    }
    public static JSONObject coldwava(String json){
        JSONObject jsonObj = JSONObject.fromObject(json);
        JSONArray data = jsonObj.getJSONArray("data");//得到json中的data数据
        System.out.println(data);
        List lowesttem = new ArrayList();
        List highesttem = new ArrayList();
        int k=0,index =0,index1 =0;
        double lowtem =0,hightem = 0;//定义最低最高温度
        double change = 0 ;
        int chaindex = 0;
        for(int i = 0; i< data.size(); i++){
            JSONObject jo = data.getJSONObject(i);
            Double highesttemperature = jo.getDouble("highest_temperature");
            Double lowesttemperature = jo.getDouble("lowest_temperature");
            lowesttem.add(lowesttemperature);
            highesttem.add(highesttemperature);
        }
        for (k=1;k<lowesttem.size();k++){                 //求出最高气温和最低气温
            lowtem = (double) lowesttem.get(0);
            Double low = (Double) lowesttem.get(k);
            hightem= (double) highesttem.get(k-1);
            Double high = (Double) highesttem.get(k);
            if(low<lowtem){
                lowtem=low;
                index = k ;
            }
            if(high>hightem){
                hightem = high;
                index1 = k;
            }
        }
        for(k = 0; k<lowesttem.size()-3;k++){        //确定寒潮发生的时间
            Double low1 = (Double) lowesttem.get(k);
            Double low2 = (Double) lowesttem.get(k+1);
            Double low3 = (Double) lowesttem.get(k+2);
            Double low4 = (Double) lowesttem.get(k+3);
           if(low1-low2 >= 8 && low2 <= 4){
               change = low1 - low2 ;
               chaindex = k ;
               break;
           }else if(low1 - low3 >= 10 && low3 <= 4){
               change = low1 -low3 ;
               chaindex = k ;
               break;
           }else if (low1 - low4 >= 12 && low4 <= 4){
               change = low1 - low4 ;
               chaindex= k ;
               break;
           }
        }


        JSONObject lowesttemobj = data.getJSONObject(index);

        JSONObject highesttemobj = data.getJSONObject(index1);
        JSONObject coldwavetemobj = data.getJSONObject(chaindex);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("台名",lowesttemobj.get("stationName"));
        jsonObject1.put("台号",lowesttemobj.get("stationNum"));
        jsonObject1.put("观测时间",coldwavetemobj.get("Information_time"));
        jsonObject1.put("降幅温度",change);
        jsonObject1.put("最低气温",lowtem);
        jsonObject1.put("最低气温出现时间",lowesttemobj.get("Information_time"));
        jsonObject1.put("最高气温",hightem);
        jsonObject1.put("最高气温出现时间",highesttemobj.get("Information_time"));
        return jsonObject1;
    }

}
