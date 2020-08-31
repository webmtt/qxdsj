package com.thinkgem.jeesite.modules.statistics.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.DateUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlData;
import com.thinkgem.jeesite.modules.recordquery.service.FtpUrlConfService;
import com.thinkgem.jeesite.modules.recordquery.service.FtpUrlDataService;

@Controller
//@RequestMapping(value="/statistics/statisticsDownloadTotal")
@RequestMapping("/statistics/statisticsDownloadTotal")
public class StatisticsDownloadTotalController extends BaseController{
	private String timeType, ordername, ordertype;
	private String start_time1;
	private String end_time1;
	
	@Autowired
	private FtpUrlDataService ftpUrlDataService;
	@Autowired
	private FtpUrlConfService ftpUrlConfService;
	
	
	
	@RequestMapping("/statisticsDownloadTotalList")
	public String statisticsDownloadTotalList(Model model) {
		return "modules/statistics/statisticsDownloadTotal";
	}
	@RequestMapping("/statisticsDownloadClassList")
	public String statisticsDownloadClassList(Model model) {
		return "modules/statistics/statisticsDownloadClass";
	}
	@RequestMapping("/statisticsDownloadProvinceList")
	public String statisticsDownloadProvinceList(Model model) {
		return "modules/statistics/statisticsDownloadProvince";
	}
	@RequestMapping("/statisticsDownloadCenterList")
	public String statisticsDownloadCenterList(Model model) {
		return "modules/statistics/statisticsDownloadCenter";
	}
	
	//=======================按总量统计===============================//
	@RequestMapping("/statTotalPictureByTime2")
	public String statTotalPictureByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
		JSONArray ja_x = new JSONArray();
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
			}
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"8", null, null, "ASC");
			}else if (timeType.equals("4")) {
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"4", null, null, "ASC");
			}else if (timeType.equals("6")) {
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time, "6", null, null, "ASC");
			}else if(timeType.equals("8")){
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time, "8", null, null, "ASC");
			}
			
			for(int i=0;i<statList.size();i++){
				ja.add(i,statList.get(i).getAccessDate());
				ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			jo.put("time", ja);
			jo.put("sum", ja_x);
			json = jo.toString();
			renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statTotalListByTime2")
	public String statTotalListByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,Integer page,Integer rows,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			List<FtpUrlData> statList1 =new ArrayList<FtpUrlData>();
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList1 = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,timeType, null, null, null);
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,timeType, page, rows, null);
			}else if (timeType.equals("4")) {
				statList1 = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"4", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"4",  page, rows,null);
			}else if (timeType.equals("6")) {
				statList1 = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"6", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"6", page, rows, null);
			}else if (timeType.equals("8")) {
				statList1 = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"8", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,"8",page, rows, null);
			}
			for(int i=0;i<statList.size();i++){
				jo.put("time",statList.get(i).getAccessDate());
				jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
				ja.add(i,jo);
			}
			JSONObject jobject = new JSONObject();
			jobject.put("total",statList1.size());
			jobject.put("rows", ja);
			json = jobject.toString();
			renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	//=======================按分类统计===============================//
	@RequestMapping("/statClassByTime_picture2")
	public void statClassByTime_picture2(String start_time,String end_time,String timeType,String org,
			HttpServletRequest request,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1);
		String json = "";
		List statList=new ArrayList();;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8", null, null, "ASC",org);
			}else if(timeType.equals("4")){
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"4", null, null, "ASC",org);
			}else if(timeType.equals("6")){
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"6", null, null, "ASC",org);
			}else if(timeType.equals("8")){
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8", null, null, "ASC",org);
			}
				
			List dataCodeList=(List)statList.get(0);
			List chnNameList=(List)statList.get(1);
			List downSizeList=(List)statList.get(2);
//			List accessDateList=(List)statList.get(3);
			int count =0;
			
			BigDecimal sum = new BigDecimal(0);
			JSONArray ja_Picture = new JSONArray();			
			JSONArray ja_temp = new JSONArray();
			for(int i=0;i<dataCodeList.size();i++){
				ja_temp.add(0, chnNameList.get(i));
				ja_temp.add(1, new  BigDecimal(Double.valueOf(downSizeList.get(i).toString())/1024/1024/1024));
//				ja_temp.add(2, accessDateList.get(i));
				ja_Picture.add(count++,ja_temp);
				sum = sum.add(new  BigDecimal(Double.valueOf(downSizeList.get(i).toString())/1024/1024/1024));
			}
			 json = ja_Picture.toString();
			 renderText(json,response);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
}

	@RequestMapping("/statClassByTime_list2")
	public String statClassByTime_list2(String start_time,String end_time,String timeType,Integer page,Integer rows,String org,
			HttpServletRequest request,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		String json = "";
		List statList=new ArrayList();
		List statList1=new ArrayList();
		try {
			
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			rows = (rows==null?10:rows);
			page = (page==null?1:page);
			org = (org==null?"0":org);
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList1=ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8",null, null, null,org);
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8",page, rows, null,org);
			}else if(timeType.equals("4")){
				statList1=ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"4",null, null, null,org);
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"4", page, rows, null,org);
			}else if(timeType.equals("6")){
				statList1=ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"6",null, null, null,org);
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"6",page, rows, null,org);
			}else if(timeType.equals("8")){
				statList1=ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8",null, null, null,org);
				statList = ftpUrlDataService.getDownLoadSumClassByTime2(start_time, end_time,"8", page, rows, null,org);
			}
			
			List dataCodeList=(List)statList.get(0);
			List chnNameList=(List)statList.get(1);
			List downSizeList=(List)statList.get(2);
//			List accessDateList=(List)statList.get(3);
			
			List chnNameList1=(List)statList1.get(1);
			
			JSONObject jo_List = new JSONObject();
			JSONArray ja_List = new JSONArray();
			int count =0;
			
			for(int i=0;i<dataCodeList.size();i++){
				jo_List.put("title", chnNameList.get(i));
				jo_List.put("orderNumber",(new  BigDecimal(Long.valueOf(downSizeList.get(i).toString())).divide(unit_TB, 2, RoundingMode.HALF_UP))+"GB");
//				jo_List.put("time", accessDateList.get(i));
				ja_List.add(count++,jo_List);
			}
			
			JSONObject joTotal = new JSONObject();
			joTotal.put("total", chnNameList1.size());
			joTotal.put("rows", ja_List);
			 json = joTotal.toString();
			 renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//=======================按省、大院统计===============================//
	//获取省份
	@RequestMapping("/getOrgType")
	public void getOrgType(HttpServletResponse response,HttpServletRequest Request, Model model){
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		List<String[]> list = ftpUrlDataService.getOrgAll();
		jo.put("orgValue", 0 + "");
		jo.put("orgName", "全部");
		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj=list.get(i);
			jo.put("orgValue", obj[0].toString());
			jo.put("orgName", obj[0].toString());
			ja.add(i + 1, jo);
		}
		json = ja.toString();
		renderText(json, response);
	}
	
	
	@RequestMapping("/statProvincePictureByTime3")
	public void statProvincePictureByTime3(HttpServletRequest request,String start_time,String end_time,String timeType,String org,HttpServletResponse response, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
	  	JSONObject jo = new JSONObject();
	  	JSONArray ja_x = new JSONArray();
	  	String json = "";
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if(org==null||org.equals("")){
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,null, null, null, "ASC");
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getOrg());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response); 
			}else if(org.equals(0+"")){//选择全部
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("4")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("6")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("8")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}
				
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getOrg());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}else{
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("4")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("6")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("8")){
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}
				
				//省
				for(int i=0;i<statList.size();i++){
//					ja.add(i,statList.get(i).getOrg());
					ja.add(i,statList.get(i).getAccessDate());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response); 
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/statProvinceListByTime3")
	public String statProvinceListByTime3(HttpServletRequest request,String start_time,String end_time,String timeType,String org,Integer page,Integer rows,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	  	JSONObject jo = new JSONObject();
	  	String json = "";
	  	JSONObject jobject = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList1=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			if(org==null||org.equals("")){
				statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,null, null, null, "ASC");
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, page, rows, "ASC");
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getOrg());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}else if(org.equals(0+"")){//选择全部
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}else if(timeType.equals("4")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"4",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"4",org, page, rows, "ASC");
				}else if(timeType.equals("6")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"6",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"6",org, page, rows, "ASC");
				}else if(timeType.equals("8")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getOrg());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}else{
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType,org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}else if(timeType.equals("4")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"4",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"4",org, page, rows, "ASC");
				}else if(timeType.equals("6")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"6",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"6",org, page, rows, "ASC");
				}else if(timeType.equals("8")){
					statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getAccessDate());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
//				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	
	@RequestMapping("/statCenterPictureByTime3")
	public String statCenterPictureByTime3(HttpServletRequest request,String start_time,String end_time,String timeType,String org,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		String json = "";
		JSONArray ja= new JSONArray(); 
	  	JSONObject jo = new JSONObject();
	  	JSONArray ja_x = new JSONArray();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if(org==null||org.equals("")){
				statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,null, null, null, null);
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getOrg());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}else if(org.equals(0+"")){//选择全部
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("4")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, null, null, "ASC");
				}else if(timeType.equals("6")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, null, null, "ASC");
				}else if(timeType.equals("8")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, null, null, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getOrg());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}else{
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,org, null, null, "ASC");
				}else if(timeType.equals("4")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, null, null, "ASC");
				}else if(timeType.equals("6")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, null, null, "ASC");
				}else if(timeType.equals("8")){
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, null, null, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getAccessDate());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statCenterListByTime3")
	public String statCenterListByTime3(HttpServletRequest request,String start_time,String end_time,String timeType,String org,Integer page,Integer rows,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    String json = "";
	    JSONArray ja= new JSONArray();
	  	JSONObject jo = new JSONObject();
	  	JSONObject jobject = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList1=new ArrayList<FtpUrlData>(); 
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			
			if(org==null||org.equals("")){
				statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,null, null, null, "ASC");
				statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",null, page, rows, "ASC");
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getOrg());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}else if(org.equals(0+"")){
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}else if(timeType.equals("4")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, page, rows, "ASC");
				}else if(timeType.equals("6")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, page, rows, "ASC");
				}else if(timeType.equals("8")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getOrg());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}else{
				if(timeType==null||timeType.equals("")){
					timeType = "8";
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType,org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}else if(timeType.equals("4")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"4",org, page, rows, "ASC");
				}else if(timeType.equals("6")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"6",org, page, rows, "ASC");
				}else if(timeType.equals("8")){
					statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, null, null, "ASC");
					statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,"8",org, page, rows, "ASC");
				}
				for(int i=0;i<statList.size();i++){
					jo.put("time",statList.get(i).getAccessDate());
					jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/*===================================================================*/
	
	@RequestMapping("/statProvincePictureByTime2")
	public void statProvincePictureByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,HttpServletResponse response, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
	  	JSONObject jo = new JSONObject();
	  	String json = "";
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,timeType, null, null, "ASC");
			}else if(timeType.equals("4")){
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,timeType, null, null, "ASC");
			}else if(timeType.equals("6")){
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,timeType, null, null, "ASC");
			}else if(timeType.equals("8")){
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,timeType, null, null, "ASC");
			}
			JSONArray ja_x = new JSONArray();
			//省
			for(int i=0;i<statList.size();i++){
				ja.add(i,statList.get(i).getOrg());
				ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			jo.put("province", ja);
			jo.put("sum", ja_x);
			json = jo.toString();
			renderText(json,response); 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/statProvinceListByTime2")
	public String statProvinceListByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,Integer page,Integer rows,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	  	JSONObject jo = new JSONObject();
	  	String json = "";
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList1=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"8", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"8", page, rows, null);
			}else if(timeType.equals("4")){
				statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"4", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"4", page, rows, null);
			}else if(timeType.equals("6")){
				statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"6", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"6", page, rows, null);
			}else if(timeType.equals("8")){
				statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"8", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumProvinceByTime3(start_time, end_time,"8", page, rows, null);
			}
			//总计
//			BigDecimal downsum=ftpUrlDataService.getDownLoadProvinceByTime(start_time, end_time);
//			jo.put("province","总计");
//			jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
//			ja.add(0,jo);
//			for(int i=1;i<statList.size()+1;i++){
//				jo.put("province",statList.get(i-1).getOrg());
//				jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
//				ja.add(i,jo);
//			}
			//没有总计
			for(int i=0;i<statList.size();i++){
				jo.put("province",statList.get(i).getOrg());
				jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
				ja.add(i,jo);
			}
			
			JSONObject jobject = new JSONObject();
			jobject.put("total",statList1.size());
			jobject.put("rows", ja);
			json = jobject.toString();
			renderText(json,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	
	@RequestMapping("/statCenterPictureByTime2")
	public String statCenterPictureByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		String json = "";
		JSONArray ja= new JSONArray(); 
	  	JSONObject jo = new JSONObject();
	  	JSONArray ja_x = new JSONArray();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,timeType, null, null, "ASC");
			}else if(timeType.equals("4")){
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"4", null, null, "ASC");
			}else if(timeType.equals("6")){
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"6", null, null, "ASC");
			}else if(timeType.equals("8")){
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"8", null, null, "ASC");
			}
			for(int i=0;i<statList.size();i++){
				ja.add(i,statList.get(i).getOrg());
				ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			jo.put("center", ja);
			jo.put("sum", ja_x);
			json = jo.toString();
			renderText(json,response); 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statCenterListByTime2")
	public String statCenterListByTime2(HttpServletRequest request,String start_time,String end_time,String timeType,Integer page,Integer rows,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    String json = "";
	    JSONArray ja= new JSONArray();
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList1=new ArrayList<FtpUrlData>(); 
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			
			if(timeType==null||timeType.equals("")){
				timeType = "8";
				statList1 = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,timeType, null, null, null);
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,timeType, page, rows, null);
			}else if(timeType.equals("4")){
				statList1 = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"4", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"4", page, rows, null);
			}else if(timeType.equals("6")){
				statList1 = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"6", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"6", page, rows, null);
			}else if(timeType.equals("8")){
				statList1 = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"8", null, null, null);
				statList = ftpUrlDataService.getDownLoadSumCenterByTime2(start_time, end_time,"8", page, rows, null);
			}
			//总计
//			BigDecimal downsum=ftpUrlDataService.getDownLoadCenterByTime(start_time, end_time);
//			jo.put("center","总计");
//			jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
//			ja.add(0,jo);
//			for(int i=1;i<=statList.size();i++){
//				jo.put("center",statList.get(i-1).getOrg());
//				jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
//				ja.add(i,jo);
//			}
			//无总计
			for(int i=0;i<statList.size();i++){
				jo.put("center",statList.get(i).getOrg());
				jo.put("sum",statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
				ja.add(i,jo);
			}
			
			JSONObject jobject = new JSONObject();
			jobject.put("total",statList1.size());
			jobject.put("rows", ja);
			json = jobject.toString();
			renderText(json,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	
//============================================================//	
	@RequestMapping("/statYears")
	public String statYears(String start_time,String end_time,String statUnit,String statYear, HttpServletRequest request,HttpServletResponse response,HttpServletRequest Request, Model model) {
		String json = "";
	  	JSONObject jo = new JSONObject();
		JSONArray ja= new JSONArray(); 
		List<String> list = ftpUrlDataService.getYears();
		jo.put("yearValue", 0+"");
		jo.put("yearName", "请选择");	
		ja.add(0, jo);
		for(int i=0;i<list.size();i++){
			jo.put("yearValue", list.get(i));
			jo.put("yearName", list.get(i)+"年");
			ja.add(i+1, jo);
		}
		 json = ja.toString();
		 renderText(json,response);
		return null;
	}
	
	@RequestMapping("/statTotalPictureByTime")
	public String statTotalPictureByTime(HttpServletRequest request,String start_time,String end_time,String statUnit,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time = DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
			}
			if(timeType==null||timeType.equals("")){
				timeType = "8";
			}
			if(statUnit==null||statUnit.equals("")){
			    statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,timeType, null, null, "ASC");
				JSONArray ja_x = new JSONArray();
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getAccessDate());
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}else if (statUnit.equals("1")) {
				statList = ftpUrlDataService.getDownLoadSumByTime("20010101", end_time,"4", null, null, "ASC");
				JSONArray ja_x = new JSONArray();
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getAccessDate()+"年");
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}else if (statUnit.equals("2")) {
				statList = ftpUrlDataService.getDownLoadSumByTime(statYear+"0101", statYear+"1231", "6", null, null, "ASC");
				JSONArray ja_x = new JSONArray();
				for(int i=0;i<statList.size();i++){
					ja.add(i,statList.get(i).getAccessDate().substring(4, 6)+"月");
					ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				json = jo.toString();
				renderText(json,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statTotalListByTime")
	public String statTotalListByTime(HttpServletRequest request,String start_time,String end_time,String statUnit,Integer page,Integer rows,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time = DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
			}
			if(timeType==null||timeType.equals("")){
				timeType = "8";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			List<FtpUrlData> statList1 = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,timeType, null, null, null);
			statUnit = (statUnit==null?"":statUnit);
			if(statUnit==null||statUnit.equals("")){
				statList = ftpUrlDataService.getDownLoadSumByTime(start_time, end_time,timeType, page, rows, null);
				BigDecimal downsum=ftpUrlDataService.getDownLoadTotalByTime(start_time, end_time);
				jo.put("time","总计");
				jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
				ja.add(0,jo);
				for(int i=1;i<=statList.size();i++){
					jo.put("time",statList.get(i-1).getAccessDate());
					jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json,response);
			}else if (statUnit.equals("1")) {
				statList = ftpUrlDataService.getDownLoadSumByTime("20010101", end_time,"4",  page, rows,null);
				BigDecimal downsum=ftpUrlDataService.getDownLoadTotalByTime(start_time, end_time);
				jo.put("time","总计");
				jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
				ja.add(0,jo);
				for(int i=1;i<=statList.size();i++){
					jo.put("time",statList.get(i-1).getAccessDate()+"年");
					jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				json = ja.toString();
				renderText(json,response);
			}else if (statUnit.equals("2")) {
				statList = ftpUrlDataService.getDownLoadSumByTime(statYear+"0101", statYear+"1231","6", null, null, null);
				BigDecimal downsum=ftpUrlDataService.getDownLoadTotalByTime(statYear+"0101", statYear+"1231");
				jo.put("time","总计");
				jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
				ja.add(0,jo);
				for(int i=1;i<=statList.size();i++){
					jo.put("time",statList.get(i-1).getAccessDate().substring(4, 6)+"月");
					jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
					ja.add(i,jo);
				}
				json = ja.toString();
				renderText(json,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	
	@RequestMapping("/statClassByTime_picture")
	public String statClassByTime_picture(String start_time,String end_time,String statUnit,String statYear, HttpServletRequest request,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1);
		String json = "";
		List statList;
		try {
			timeType = "8";
			if(start_time==null||start_time.equals("")){
				start_time="0";			
			}
			if(end_time==null||start_time.equals("")){
				end_time="99999999";
			}	
			//根据下载量排序，根据资料分组
			statList = ftpUrlDataService.getDownLoadSumClassByTime(start_time, end_time);
			List dataCodeList=(List)statList.get(0);
			List chnNameList=(List)statList.get(1);
			List downSizeList=(List)statList.get(2);
			int count =0;
			//总的订单量
			BigDecimal total = ftpUrlDataService.getDownLoadTotalByTime(start_time, end_time);
			BigDecimal sum = new BigDecimal(0);
			JSONArray ja_Picture = new JSONArray();			
			JSONArray ja_temp = new JSONArray();
			for(int i=0;i<dataCodeList.size();i++){
				ja_temp.add(0, chnNameList.get(i));
				ja_temp.add(1, new  BigDecimal(Long.valueOf(downSizeList.get(i).toString())));
				ja_Picture.add(count++,ja_temp);
				sum = sum.add(new  BigDecimal(Long.valueOf(downSizeList.get(i).toString())));
			}
			 json = ja_Picture.toString();
			 renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}

	@RequestMapping("/statClassByTime_list")
	public String statClassByTime_list(String start_time,String end_time,String statUnit,String statYear,Integer page,Integer rows, HttpServletRequest request,HttpServletResponse response,HttpServletRequest Request, Model model) {
		start_time1 = start_time;
		end_time1 = end_time;
		BigDecimal unit_TB = new BigDecimal(1073741824);
		String json = "";
		List statList;
		try {
			timeType = "8";
			if(start_time==null||start_time.equals("")){
				start_time="0";	
			}
			if(end_time==null||start_time.equals("")){
				end_time="99999999";
			}
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			statList = ftpUrlDataService.getDownLoadSumClassByTime(start_time, end_time, timeType, ordername, ordertype,page,rows);
			
			List dataCodeList=(List)statList.get(0);
			List chnNameList=(List)statList.get(1);
			List downSizeList=(List)statList.get(2);
			
			List<FtpUrlData> statList1 = ftpUrlDataService.getDownLoadSumClassByTime(start_time, end_time,timeType, ordername, ordertype,null,null);
			List chnNameList1=(List)statList1.get(0);
			
			JSONObject jo_List = new JSONObject();
			JSONArray ja_List = new JSONArray();
			int count =0;
			
			for(int i=0;i<dataCodeList.size();i++){
				jo_List.put("title", chnNameList.get(i));
				jo_List.put("orderNumber",(new  BigDecimal(Long.valueOf(downSizeList.get(i).toString())).divide(unit_TB, 2, RoundingMode.HALF_UP))+"GB");
				ja_List.add(count++,jo_List);
			}
			
			JSONObject joTotal = new JSONObject();
			joTotal.put("total", chnNameList1.size());
			joTotal.put("rows", ja_List);
			 json = joTotal.toString();
			 renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statProvincePictureByTime")
	public String statProvincePictureByTime(HttpServletRequest request,String start_time,String end_time,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
		JSONArray jb= new JSONArray(); 
		
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList2=new ArrayList<FtpUrlData>();
		
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time="99999999";
			}
			
			timeType = "8";
			statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType, null, null, "ASC");
			statList2=ftpUrlDataService.getDownLoadSumProvinceByTime2(start_time, end_time,timeType, null, null, "ASC");
			
			JSONArray ja_x = new JSONArray();
			
			//大元总计
			
			if(statList2.size()==0){
				ja.add(0, "");
				ja_x.add(0,"");
			}else{
				ja.add(0, "大院");
				ja_x.add(0, statList2.get(0).getDownSize()==null?0:((new BigDecimal(statList2.get(0).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			
			
			//省
			for(int i=1;i<=statList.size();i++){
				ja.add(i,statList.get(i-1).getOrg());
				ja_x.add(i,statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			jo.put("province", ja);
			jo.put("sum", ja_x);
			json = jo.toString();
			renderText(json,response); 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("/statProvinceListByTime")
	public String statProvinceListByTime(HttpServletRequest request,String start_time,String end_time,String statUnit,Integer page,Integer rows,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		List<FtpUrlData> statList2=new ArrayList<FtpUrlData>();
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time="99999999";
			}
			timeType = "8";
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			List<FtpUrlData> statList1 = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType, null, null, null);
			
			statList = ftpUrlDataService.getDownLoadSumProvinceByTime(start_time, end_time,timeType, page, rows, null);
			BigDecimal downsum=ftpUrlDataService.getDownLoadProvinceByTime(start_time, end_time);
			jo.put("province","总计");
			jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
			ja.add(0,jo);
			
			statList2=ftpUrlDataService.getDownLoadSumProvinceByTime2(start_time, end_time,timeType, null, null, "ASC");
			
			
			if(statList2.size()==0){
				jo.put("province","");
				jo.put("sum","");
			}else{
				jo.put("province","大院");
				jo.put("sum",statList2.get(0).getDownSize()==null?0:((new BigDecimal(statList2.get(0).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			
			
			ja.add(1,jo);
			
			for(int i=2;i<statList.size()+2;i++){
				jo.put("province",statList.get(i-2).getOrg());
				jo.put("sum",statList.get(i-2).getDownSize()==null?0:((new BigDecimal(statList.get(i-2).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
				ja.add(i,jo);
			}
			JSONObject jobject = new JSONObject();
			jobject.put("total",statList1.size()+1);
			jobject.put("rows", ja);
			json = jobject.toString();
			renderText(json,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@RequestMapping("/statCenterPictureByTime")
	public String statCenterPictureByTime(HttpServletRequest request,String start_time,String end_time,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
		BigDecimal unit_TB = new BigDecimal(1073741824);
		JSONArray ja= new JSONArray(); 
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time="99999999";
			}
			
			timeType = "8";
			statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType, null, null, "ASC");
			JSONArray ja_x = new JSONArray();
			for(int i=0;i<statList.size();i++){
				ja.add(i,statList.get(i).getOrg());
				ja_x.add(i,statList.get(i).getDownSize()==null?0:((new BigDecimal(statList.get(i).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue()));
			}
			jo.put("center", ja);
			jo.put("sum", ja_x);
			json = jo.toString();
			renderText(json,response); 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/statCenterListByTime")
	public String statCenterListByTime(HttpServletRequest request,String start_time,String end_time,String statUnit,Integer page,Integer rows,String statYear,HttpServletResponse response,HttpServletRequest Request, Model model) {
	    BigDecimal unit_TB = new BigDecimal(1073741824);
	    JSONArray ja= new JSONArray();
	    String json = "";
	  	JSONObject jo = new JSONObject();
		List<FtpUrlData> statList=new ArrayList<FtpUrlData>();
		try {
			if(start_time==null||start_time.equals("")){
				start_time="20090101";			
			}
			if(end_time==null||end_time.equals("")){
				end_time="99999999";
			}
			timeType = "8";
			page = (page==null?1:page);
			rows = (rows==null?10:rows);
			List<FtpUrlData> statList1 = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType, null, null, null);
			
			statList = ftpUrlDataService.getDownLoadSumCenterByTime(start_time, end_time,timeType, page, rows, null);
			BigDecimal downsum=ftpUrlDataService.getDownLoadCenterByTime(start_time, end_time);
			jo.put("center","总计");
			jo.put("sum",downsum.divide(unit_TB,2,BigDecimal.ROUND_HALF_UP).doubleValue()+"GB");
			ja.add(0,jo);
			for(int i=1;i<=statList.size();i++){
				jo.put("center",statList.get(i-1).getOrg());
				jo.put("sum",statList.get(i-1).getDownSize()==null?0:((new BigDecimal(statList.get(i-1).getDownSize()).divide(unit_TB,2,BigDecimal.ROUND_HALF_UP)).doubleValue())+"GB");
				ja.add(i,jo);
			}
			JSONObject jobject = new JSONObject();
			jobject.put("total",statList1.size());
			jobject.put("rows", ja);
			json = jobject.toString();
			renderText(json,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
}
