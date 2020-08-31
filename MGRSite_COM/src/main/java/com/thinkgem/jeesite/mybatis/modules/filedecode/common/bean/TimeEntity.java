package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;



import java.io.Serializable;


public class TimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String year;
    private String month;
    private String day;

    public void setDay(String day) {
        if(day.length()<2){
            day="0"+day;
        }
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public void setMonth(String month) {
        if(month.length()<2){
            month="0"+month;
        }
        this.month = month;
    }

    public String getTime() {
        return year + "-" + month + "-" + day;
    }

    public Integer getDayCountOnMonth(){
        if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)){
            return 30;
        }else if("02".equals(month)&&Integer.parseInt(year)%4==0){
            return 29;
        }else if("02".equals(month)){
            return 28;
        }else{
            return 31;
        }
    }
}
