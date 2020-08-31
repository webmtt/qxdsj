package com.thinkgem.jeesite.mybatis.modules.filedecode.service;


import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.ReflectUtil;
import com.thinkgem.jeesite.mybatis.modules.report.dao.XuGuSearchDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 去年年值数据统计
 * @author yang.kq
 * @version 1.0
 * @date 2020/4/21 9:48
 */
@Service
public class YearInfoHandleService {
    @Resource
    private XuGuSearchDao xuguDao;


    /**
     *获取去年月值信息
     */
    public List<MonthValueTab> getLastYearMonthData(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);    //获取年
        List<MonthValueTab> result = new ArrayList<>();
        MonthValueTab monthValueTab = null;
        try {
            List<Map<String, Object>> list = xuguDao.getLastYearMonthData(year-1);
            Iterator<Map<String, Object>> entry = list.iterator();
            Map<String,String> mm=ReflectUtil.getFiledsInfo(new MonthValueTab());
            if (entry.hasNext()) {
                monthValueTab=new MonthValueTab();
                Map<String, Object> map = entry.next();
                Set<String> key = map.keySet();
                for (String keys : key) {
                    Object value = map.get(keys);
                    Object v1="999999";
                    Object v2="999999.0";
                    if(value!=null&&(!v1.equals(value+""))&&(!v2.equals(value+""))){
                       String t= mm.get(keys);
                       if("java.lang.Float".equals(t)){
                           value=Float.parseFloat(value+"");
                       }else if("java.lang.String".equals(t)){
                           value=value+"";
                       }else if("java.lang.Integer".equals(t)){
                           value=Integer.parseInt(value+"");
                       }else if("java.math.BigDecimal".equals(t)){
                           value=new BigDecimal(value+"");
                       }
                        ReflectUtil.setValue(monthValueTab, keys, value);
                    }
                }
                result.add(monthValueTab);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 年数据统计值入库
     * @param list
     */
    public void insertYearData(List<YearValueTab> list, String yearTableName) {
        xuguDao.insertYearData(list,yearTableName);
    }

    public List<DayValueTab> getLastYearDayValue(String stationNum, Integer year) {
        List<DayValueTab> result = new ArrayList<>();
        DayValueTab dayValueTab = null;
        try {
            List<Map<String, Object>> list = xuguDao.getLastYearDayData(stationNum,year);
            Iterator<Map<String, Object>> entry = list.iterator();
            if (entry.hasNext()) {
                dayValueTab=new DayValueTab();
                Map<String, Object> map = entry.next();
                Set<String> key = map.keySet();
                for (String keys : key) {
                    if(keys.toUpperCase().indexOf("Q")>-1){//质控参数不统计
                        continue;
                    }
                    Object value = map.get(keys);
                    Object v1="999999";
                    Object v2="999999.0";
                    if(value!=null&&(!v1.equals(value+""))&&(!v2.equals(value+""))) {
                        ReflectUtil.setValue(dayValueTab, "keys", value);
                    }
                }
                result.add(dayValueTab);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public List<HourValueTab> getLastYearHourValue(String stationNum, Integer year) {
        List<HourValueTab> result = new ArrayList<>();
        HourValueTab hourValueTab = null;
        try {
            List<Map<String, Object>> list = xuguDao.getLastYearHourData(stationNum,year);
            Iterator<Map<String, Object>> entry = list.iterator();
            if (entry.hasNext()) {
                hourValueTab=new HourValueTab();
                Map<String, Object> map = entry.next();
                Set<String> key = map.keySet();
                for (String keys : key) {
                    if(keys.toUpperCase().indexOf("Q")>-1){//质控参数不统计
                        continue;
                    }
                    Object value = map.get(keys);
                    Object v1="999999";
                    Object v2="999999.0";
                    if(value!=null&&(!v1.equals(value+""))&&(!v2.equals(value+""))) {
                        ReflectUtil.setValue(hourValueTab, "keys", value);
                    }
                }
                result.add(hourValueTab);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<RadiDigChnMulTab> getLastYearRadiValue(String stationNum, Integer year) {
        List<RadiDigChnMulTab> result= new ArrayList<>();
        RadiDigChnMulTab radiDigChnMulTab = null;
        try {
            List<Map<String, Object>> list = xuguDao.getLastYearRadiData(stationNum,year);
            Iterator<Map<String, Object>> entry = list.iterator();
            if (entry.hasNext()) {
                radiDigChnMulTab=new RadiDigChnMulTab();
                Map<String, Object> map = entry.next();
                Set<String> key = map.keySet();
                for (String keys : key) {
                    if(keys.toUpperCase().indexOf("Q")>-1){//质控参数不统计
                        continue;
                    }
                    Object value = map.get(keys);
                    Object v1="999999";
                    Object v2="999999.0";
                    if(value!=null&&(!v1.equals(value+""))&&(!v2.equals(value+""))) {
                        ReflectUtil.setValue(radiDigChnMulTab, "keys", value);
                    }
                }
                result.add(radiDigChnMulTab);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
