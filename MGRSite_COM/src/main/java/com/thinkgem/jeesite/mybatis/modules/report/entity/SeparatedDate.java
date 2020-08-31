package com.thinkgem.jeesite.mybatis.modules.report.entity;

import java.util.*;
/**
 * 年-月-日分离
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/31 9:38
 */
public class SeparatedDate {

    private String dbyear = "V04001";
    private String dbmonth = "V04002";
    private String dbday = "V04003";

    private int oldYear;
    private int year;
    private int oldMonth;
    private int month;
    private int oldDay;
    private int day;
    public StringBuffer sb = new StringBuffer();


    public SeparatedDate(Calendar oldDate,Calendar newDate){
        oldYear = oldDate.YEAR;
        year = newDate.YEAR;
        oldMonth = oldDate.MONTH;
        month = newDate.MONTH;
        oldDay = oldDate.DAY_OF_MONTH;
        day = newDate.DAY_OF_MONTH;

    }

    public void checkMonth(int month) throws Exception{
        if(month < 1 || month > 12)
            throw new Exception();
    }

    public void checkDay(int day) throws Exception{
        if(day < 1 || day > 31)
            throw new Exception();
    }

    public SeparatedDate(int oldYear,int oldMonth, int oldDay, int year, int month, int day){

        this.oldYear = oldYear;
        this.year = year;
        this.oldMonth = oldMonth;
        this.month = month;
        this.oldDay = oldDay;
        this.day = day;

        this.dbyear = dbyear;
        this.dbmonth = dbmonth;
        this.dbday = dbday;

    }
    public SeparatedDate(int oldYear,int oldMonth,int year, int month ){

        this.oldYear = oldYear;
        this.year = year;
        this.oldMonth = oldMonth;
        this.month = month;

        this.dbyear = dbyear;
        this.dbmonth = dbmonth;

    }
    public String toString(){

        try{
            createYear();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void createYear() throws Exception{
        if(oldYear == year){
            if(oldMonth == month){
                if(oldDay == day){
                    sb.append(" "  + dbyear + " = " + year + " and " + dbmonth + " = " + month + " and " + dbday + " = " + day);
                }
                else{
                    sb.append(" "  + dbyear + " = " + year + " and " + dbmonth + " = " + month + " and " + dbday + " between " + oldDay + " and " + day);
                }
            }
            else if(oldMonth < month){
                if(oldMonth + 1 == month){
                    createSameYear("begin");
                    sb.append(" or ");
                    createSameYear("end");
                }
                else{
                    createSameYear("begin");
                    sb.append(" or ");
                    createSameYear("middle");
                    sb.append(" or ");
                    createSameYear("end");
                }
            }
        }
        else if(oldYear < year){
            if(oldYear + 1 == year){
                createDiffYear("begin");
                sb.append(" or ");
                createDiffYear("end");
            }

            else{
                createDiffYear("begin");
                sb.append(" or ");
                createDiffYear("middle");
                sb.append(" or ");
                createDiffYear("end");
            }
        }
    }


    /*****************************************************************************
     * createDiffYear serious
     * @param control
     *****************************************************************************/
    public void createDiffYear(String control) throws Exception{
        if(control.equals("middle")){
            sb.append(" "  + dbyear + " between " + (oldYear + 1) + " and " + (year - 1));
        }

        createMonth(control);
        createDay(control);
    }

    public void createMonth(String control){
        if(control.equals("begin")){
            if(oldMonth == 12){
                int index = sb.lastIndexOf("or");
                sb.replace(index, index + 2, "");
                return ;
            }

            sb.append(" " + dbyear + " = " + oldYear + " and " + dbmonth + " between " + (oldMonth + 1) + " and 12");
        }
        else if(control.equals("end")){
            if(month == 1)
            {
                int index = sb.lastIndexOf("or");
                sb.replace(index, index + 2, "");
                return ;
            }

            sb.append(" " + dbyear + " = " + year + " and " + dbmonth + " between 1 and " + (month - 1));
        }
    }

    public void createDay(String control) throws Exception{

        if(control.equals("begin")){
            sb.append(" or " + dbyear + " = " + oldYear + " and " + dbmonth + " = " + oldMonth + " and " + dbday);
            sb.append(" between " + oldDay + " and " + getMonthDays(oldMonth));
        }
        else if(control.equals("end")){
            sb.append(" or " + dbyear + " = " + year + " and " + dbmonth + " = " + month + " and " + dbday);
            sb.append(" between 1 and " + day);
        }
    }

    /*****************************************************************************
     * end createDiffYear
     *****************************************************************************/



    /*****************************************************************************
     * the same year
     *****************************************************************************/
    public void createSameYear(String control) throws Exception{
        if(control.equals("begin")){
            sb.append(" "  + dbyear + " = " + year + " and " + dbmonth + " = " + oldMonth + " and " + dbday + " between " + oldDay + " and " + getMonthDays(oldMonth));
        }
        else if(control.equals("middle")){
            if(oldMonth >= month)
                return ;
            sb.append(" "  + dbyear + " = " + year + " and " + dbmonth + " between " + (oldMonth + 1) + " and " + (month - 1));
        }
        else if(control.equals("end")){
            sb.append(" "  + dbyear + " = " + year + " and " + dbmonth + " = " + month + " and " + dbday + " between 1 and " + day);
        }
    }

    /*****************************************************************************
     * common used
     * @param m
     * @return
     */
    public int getMonthDays(int m) throws Exception{
        if(m < 1 || m > 12)
            throw new Exception();
        if(m == 2)
            return ((year%4 == 0) && (year%100 != 0)) || (year%400 == 0) ? 29 : 28;
        if(m == 4 || m == 6 || m == 9 || m == 11)
            return 30;
        else return 31;

    }

    public static void main(String [] args){
        SeparatedDate sd = new SeparatedDate(2006,1,1,2018,1,31);

        System.out.println(sd);

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        try{
            checkDay(day);
        }
        catch(Exception e){

        }
        this.day = day;
    }

    public String getDbday() {
        return dbday;
    }

    public void setDbday(String dbday) {
        this.dbday = dbday;
    }

    public String getDbmonth() {
        return dbmonth;
    }

    public void setDbmonth(String dbmonth) {
        this.dbmonth = dbmonth;
    }

    public String getDbyear() {
        return dbyear;
    }

    public void setDbyear(String dbyear) {
        this.dbyear = dbyear;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        try{
            checkMonth(month);
        }
        catch(Exception e){

        }
        this.month = month;
    }

    public int getOldDay() {
        return oldDay;
    }

    public void setOldDay(int oldDay) {
        try{
            checkDay(oldDay);
        }
        catch(Exception e){

        }
        this.oldDay = oldDay;
    }

    public int getOldMonth() {
        return oldMonth;
    }

    public void setOldMonth(int oldMonth) {
        try{
            checkMonth(oldMonth);
        }
        catch(Exception e){

        }
        this.oldMonth = oldMonth;
    }

    public int getOldYear() {
        return oldYear;
    }

    public void setOldYear(int oldYear) {
        this.oldYear = oldYear;
    }

    public StringBuffer getSb() {
        return sb;
    }

    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}