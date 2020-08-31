package com.thinkgem.jeesite.modules.statistics.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.access.Service.AccessFunConfigService;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import com.thinkgem.jeesite.modules.statistics.service.AccessDateDataService;
import com.thinkgem.jeesite.modules.statistics.service.AccessDateIPService;
import com.thinkgem.jeesite.modules.sys.entity.PageAccessCode;

/**
 * 
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-12-20
 */
@Controller
@RequestMapping(value = "statistics/statisticsAccessInfo")
public class StatisticsAccessInfoController extends BaseController {
	protected BigDecimal unit_TB = new BigDecimal(1);
	protected List<PageAccessCode> datadeflist;
	private String timeType, ordername, ordertype;
	@Autowired
	private AccessDateIPService accessDateIPService;
	@Autowired
	private AccessDateDataService accessDateDataService;
	@Autowired
	private AccessFunConfigService accessFunConfigService;
    
	
	@RequestMapping(value = "statisticsAccessInfoTotalList")
	public String statisticsAccessInfoTotalList(Model model) {
		return "modules/statistics/statisticsAccessInfoTotal";
	}
	@RequestMapping(value = "statisticsAccessInfoProList")
	public String statisticsAccessInfoProList(Model model) {
		return "modules/statistics/statisticsAccessInfoPro";
	}
	@RequestMapping(value = "statisticsAccessInfoOrgList")
	public String statisticsAccessInfoOrgList(Model model) {
		return "modules/statistics/statisticsAccessInfoOrg";
	}
	@RequestMapping(value = "statisticsAccessInfoItemList")
	public String statisticsAccessInfoItemList(Model model) {
		return "modules/statistics/statisticsAccessInfoItem";
	}
	//数据下载的统计
	@RequestMapping(value = "statisticsAccessInfoItemList2")
	public String statisticsAccessInfoItemList2(Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar   cal_1=Calendar.getInstance();//获取当前日期 
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		String startTime = sdf.format(cal_1.getTime());
		String endTime=sdf.format(new Date());
		 model.addAttribute("startTime", startTime);
		 model.addAttribute("endTime", endTime);
		return "modules/statistics/statisticsAccessInfoItem2";
	}
	@RequestMapping(value = "statisticsAccessInfoTimeList")
	public String statisticsAccessInfoTimeList(Model model) {
		return "modules/statistics/statisticsAccessInfoTime";
	}
	/**
	 * 总访问量图形---
	 * 
	 * @param request
	 * @param start_time
	 * @param end_time
	 * @param statUnit 统计方式  逐月 其他没有 
	 * @param statYear
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statTotalPictureByTime")
	public String statTotalPictureByTime(HttpServletRequest request, String start_time, String end_time,
			String statUnit, String statYear,String sourceType, HttpServletResponse response, HttpServletRequest Request, Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//总量 逐月
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
				statList = accessDateDataService.getSumByTime(start_time, end_time, timeType, null, null, "ASC","0");
				//ja_x -- ip   ja_y -- pv
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("3")) {
				//逐日
				statList = accessDateDataService.getSumByTime(start_time, end_time, "8", null, null, "ASC",sourceType);
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("2")) {
				//逐月
				statList = accessDateDataService.getSumByTime(start_time, end_time, "6", null, null, "ASC",sourceType);
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString().substring(4, 6)+"月");
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("1")) {
				//分小时
				statList = accessDateDataService.getSumByTime(start_time, end_time, "1", null, null, "ASC",sourceType);
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					String time=obj[0].toString().substring(6, 8)+"日"+obj[0].toString().substring(8, 10)+"时";
					ja.add(i,time);
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 总访问量表格
	 * 
	 * @param request
	 * @param start_time
	 * @param end_time
	 * @param statUnit
	 * @param page
	 * @param rows
	 * @param statYear
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statTotalListByTime")
	public String statTotalListByTime(HttpServletRequest request, String start_time, String end_time, String statUnit,
			Integer page, Integer rows, String statYear,String sourceType, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		String json = "";
		List<Object[]> statList;
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			page = (page == null ? 1 : page);
			rows = (rows == null ? 10 : rows);
			statUnit = (statUnit == null ? "" : statUnit);
			if (statUnit == null || statUnit.equals("")) {
				List<Object[]> statList1 = accessDateDataService.getSumByTime(start_time, end_time,timeType, null, null, null,"0");
				statList = accessDateDataService.getSumByTime(start_time, end_time, timeType, page, rows, null,"0");
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString());
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jo.put("sum2",obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja.add(i, jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json, response);
			} else if (statUnit.equals("3")) {
				List<Object[]> statList1  = accessDateDataService.getSumByTime(start_time, end_time, "8", null, null, null,sourceType);
				statList = accessDateDataService.getSumByTime(start_time, end_time, "8", page, rows, null,sourceType);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString());
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jo.put("sum2",obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja.add(i, jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json, response);
			} else if (statUnit.equals("2")) {
				
				statList = accessDateDataService.getSumByTime(start_time, end_time, "6", page, rows, null,sourceType);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString().substring(4, 6)+"月");
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jo.put("sum2",obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja.add(i, jo);
				}
				renderText(ja.toString(), response);
			}else if (statUnit.equals("1")) {
				List<Object[]> statList1 = accessDateDataService.getSumByTime(start_time, end_time,"1", null, null, null,sourceType);
				statList = accessDateDataService.getSumByTime(start_time, end_time, "1", page, rows, null,sourceType);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					String time=obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6)+"-"+obj[0].toString().substring(6, 8)
							+" "+obj[0].toString().substring(8, 10);
					jo.put("time",time);
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jo.put("sum2",obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja.add(i, jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * 分省和国家级  进行分时间查询
	 * @param start_time
	 * @param end_time
	 * @param statUnit 0时是按月查询
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimePic")
	public String statProByTimePic(String start_time, String end_time, String statUnit, String statYear, Integer page,
			Integer rows,String province, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
			Object obj2=CacheMapUtil.getCache("Access_statList", null);
				if(obj2!=null){
					statList=(List<Object[]>)CacheMapUtil.getCache("Access_statList", null);
				}else{
					List list = accessDateDataService.getOrgId("province");
					statList = accessDateIPService.getSumByPro(list);
					CacheMapUtil.putCache("Access_statList", statList, 2880);
				}
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if("1".equals(statUnit)){
				//日
				if("0".equals(province)){
					List list = accessDateDataService.getOrgId("province");
					statList = accessDateIPService.getSumByPro(list,start_time, end_time,"1");
					//statList = accessDateIPService.getSumByTime(start_time, end_time, "1", null, null, "ASC",province);
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						//String time=obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6)+"-"+obj[0].toString().substring(6, 8);
						jt.put("name",obj[0].toString());
						jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						jt.put("sum2", obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						jp.add(i, jt);
						ja.add(i, obj[0].toString());
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}else{
					statList = accessDateIPService.getSumByTime(start_time, end_time, "1", null, null, "ASC",province);
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						String time=obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6)+"-"+obj[0].toString().substring(6, 8);
						jt.put("name",time);
						jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						jt.put("sum2", obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						jp.add(i, jt);
						ja.add(i, time);
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}
			}else if("2".equals(statUnit)){
				//月
				if("0".equals(province)){
					List list = accessDateDataService.getOrgId("province");
					statList = accessDateIPService.getSumByPro(list,start_time, end_time,"2");
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						jt.put("name", obj[0].toString());
						jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						jt.put("sum2", obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						jp.add(i, jt);
						ja.add(i, obj[0].toString());
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}else{
					statList = accessDateIPService.getSumByTime(start_time, end_time, "2", null, null, "ASC",province);
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						jt.put("name", obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6));
						jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						jt.put("sum2", obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						jp.add(i, jt);
						ja.add(i, obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6));
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分省和国家级  进行分时间查询
	 * @param start_time
	 * @param end_time
	 * @param statUnit
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimeList")
	public String statProByTimeList(String start_time, String end_time, String statUnit, String statYear, Integer page,
			Integer rows, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		
		    return statYear;
	}
	/**
	 * 大院内部进行分单位 
	 * @param start_time
	 * @param end_time
	 * @param statUnit 0时是按月查询
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimeOrg")
	public String statProByTimeOrg(String start_time, String end_time, String statUnit, String statYear, Integer page,
			Integer rows,String province, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
				 Object obj2=CacheMapUtil.getCache("Access_statList2", null);
				if(obj2!=null){
					statList=(List<Object[]>)CacheMapUtil.getCache("Access_statList2", null);
				}else{
					List list = accessDateDataService.getOrgId("center");
					statList = accessDateIPService.getSumByPro(list);
					CacheMapUtil.putCache("Access_statList2", statList, 2880);
				}
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("1")){
				 if("0".equals(province)){
					//statList = accessDateIPService.getSumByTimeOrg(start_time, end_time, "1", null, null, "ASC",province);
					//ja_x -- ip   ja_y -- pv
					List list = accessDateDataService.getOrgId("center");
					statList = accessDateIPService.getSumByPro(list,start_time, end_time,"1");
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						//String time=obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6)+"-"+obj[0].toString().substring(6, 8);
						jt.put("name", obj[0].toString());
						jt.put("sum", Integer.valueOf(obj[1].toString()));
						jt.put("sum2", Integer.valueOf(obj[2].toString()));
						jp.add(i, jt);
						ja.add(i,obj[0].toString());
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}else{
					statList = accessDateIPService.getSumByTimeOrg(start_time, end_time, "1", null, null, "ASC",province);
					//ja_x -- ip   ja_y -- pv
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						String time=obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6)+"-"+obj[0].toString().substring(6, 8);
						jt.put("name", time);
						jt.put("sum", Integer.valueOf(obj[1].toString()));
						jt.put("sum2", Integer.valueOf(obj[2].toString()));
						jp.add(i, jt);
						ja.add(i,time);
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}
			
			}else if(statUnit.equals("2")){
				if("0".equals(province)){
					//statList = accessDateIPService.getSumByTimeOrg(start_time, end_time, "2", null, null, "ASC",province);
					//ja_x -- ip   ja_y -- pv
					List list = accessDateDataService.getOrgId("center");
					statList = accessDateIPService.getSumByPro(list,start_time, end_time,"2");
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						jt.put("name", obj[0].toString());
						jt.put("sum", Integer.valueOf(obj[1].toString()));
						jt.put("sum2", Integer.valueOf(obj[2].toString()));
						jp.add(i, jt);
						ja.add(i, obj[0].toString());
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}else{
					statList = accessDateIPService.getSumByTimeOrg(start_time, end_time, "2", null, null, "ASC",province);
					//ja_x -- ip   ja_y -- pv
					JSONArray jp = new JSONArray();
					for (int i = 0; i <= statList.size()-1; i++) {
						Object[] obj=statList.get(i);
						JSONObject jt = new JSONObject();
						jt.put("name", obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6));
						jt.put("sum", Integer.valueOf(obj[1].toString()));
						jt.put("sum2", Integer.valueOf(obj[2].toString()));
						jp.add(i, jt);
						ja.add(i, obj[0].toString().substring(0, 4)+"-"+obj[0].toString().substring(4, 6));
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					}
					jo.put("name", ja);
					jo.put("sum", ja_x);
					jo.put("sum2", ja_y);
					JSONObject joall = new JSONObject();
					joall.put("pic", jo.toString());
					joall.put("list", jp);
					json = jo.toString();
					renderText(joall.toString(), response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分栏目
	 * @param start_time
	 * @param end_time
	 * @param statUnit 0时是按月查询
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimeItem")
	public String statProByTimeItem(String start_time, String end_time, String statUnit,String funcItemId, String statYear, Integer page,
			Integer rows,String org, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList = new ArrayList<Object[]>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			JSONArray ja_z = new JSONArray();
			//查询栏目名称 list
			String orgname="0";
			 List<String> list=accessDateDataService.getname(org);
			 if(list.size()>0){
				 orgname=list.get(0).toString();
			 }
			if (statUnit == null || statUnit.equals("")) {
				//大栏目下的小栏目list
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				//分栏目--日
				start_time = "20160101";
				//接口获取数据 
				statList = accessDateIPService.getSumByItems1(list2,start_time, end_time,"1",org);
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					 statList2=accessDateDataService.getSumByTimeItem3(start_time, end_time,funcItemId, "6", null, null, "ASC");
				}else{
					 statList2=accessDateDataService.getSumByTimeItem4(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
					
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, sum3);
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("1")){
				//分栏目  -查询月的值
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				//分栏目--日
				if("0".equals(funcItemId)){
					statList = accessDateIPService.getSumByItems1(list2,start_time, end_time,"2",org);
				}else{
					statList = accessDateIPService.getSumByItems2(list2,start_time, end_time,"2",org);
				}
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					 statList2=accessDateDataService.getSumByTimeItem3(start_time, end_time,funcItemId, "6", null, null, "ASC");
				}else{
					 statList2=accessDateDataService.getSumByTimeItem4(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, sum3);
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("2")){
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				//分栏目--日
				if("0".equals(funcItemId)){
					statList = accessDateIPService.getSumByItems1(list2,start_time, end_time,"1",org);
				}else{
					statList = accessDateIPService.getSumByItems2(list2,start_time, end_time,"1",org);
				}
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					statList2=accessDateDataService.getSumByTimeItem3(start_time, end_time,funcItemId, "8", null, null, "ASC");
				}else{
					statList2=accessDateDataService.getSumByTimeItem4(start_time, end_time,funcItemId, "8", null, null, "ASC",orgname);
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, sum3);
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("3")){
				//分栏目  -查询月的值
				List<AccessFunConfig> list21 = accessFunConfigService.findItems(funcItemId);
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					statList2=accessDateDataService.getSumByTimeItem2(start_time, end_time,funcItemId, "6", null, null, "ASC");
					statList = accessDateDataService.getSumByTimeItem(start_time, end_time,funcItemId, "6", null, null, "ASC");
				}else{
					statList2=accessDateDataService.getSumByTimeItem22(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
					statList = accessDateDataService.getSumByTimeItem0(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
				}
				
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					if(list21.size()>0){
						name=list21.get(0).getFuncItemName();
					}
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].toString().equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", name+"("+obj[0].toString()+")");
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i,sum3);
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if (statUnit.equals("4")) {
				//分栏目--日
				List<AccessFunConfig> list21 = accessFunConfigService.findItems(funcItemId);
				if("0".equals(org)){
					statList = accessDateDataService.getSumByTimeItem(start_time, end_time,funcItemId, timeType, null, null, "ASC");
				}else{
					statList = accessDateDataService.getSumByTimeItem0(start_time, end_time,funcItemId, timeType, null, null, "ASC",orgname);
				}
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					if(list21.size()>0){
						name=list21.get(0).getFuncItemName();
					}
					jt.put("name", name+"("+obj[0].toString()+")");
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", Double.valueOf(obj[3].toString()));
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, obj[3] == null ? 0 : (Double.valueOf(obj[3].toString())));
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 具体数据集产品的ip和pv
	 * @param start_time
	 * @param end_time
	 * @param statUnit
	 * @param funcItemId
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param org
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimeItem2")
	public String statProByTimeItem2(String start_time, String end_time, String statUnit,String funcItemId, String statYear, Integer page,
			Integer rows,String org, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList = new ArrayList<Object[]>();
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			JSONArray ja_z = new JSONArray();
			//查询栏目名称 list
			String orgname="0";
			 List<String> list=accessDateDataService.getname(org);
			 if(list.size()>0){
				 orgname=list.get(0).toString();
			 }
			if (statUnit == null || statUnit.equals("")) {
				
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					statList2=accessDateDataService.getSumByTimeItem3A(start_time, end_time,funcItemId, "8", null, null, "ASC");
				}else{
					statList2=accessDateDataService.getSumByTimeItem4A(start_time, end_time,funcItemId, "8", null, null, "ASC",orgname);
				}
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				Map namemap=new HashMap();
				for(int i=0;i<list2.size();i++){
					namemap.put(list2.get(i)[0], list2.get(i)[1]);
				}
				//分栏目--日
				if("0".equals(funcItemId)){
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"1",org,funcItemId);
				}else{
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"1",org,funcItemId);
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				//排序
				Collections.sort(statList, new Comparator<Object[]>() {
					public int compare(Object[] o1, Object[] o2) {
						int map1value = Integer.valueOf(o1[2].toString());
						int map2value = Integer.valueOf(o2[2].toString());
						return map2value - map1value;
					}
				});
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				int n=0;
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if((statList2.get(j)[0]).equals(namemap.get(obj[0].toString()))){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
							
						}
					}
					if(namemap.get(obj[0].toString())==null||"".equals(namemap.get(obj[0].toString()))){
						continue;
					}
					jt.put("name", namemap.get(obj[0].toString()));
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(n, jt);
					if(n<5){
						ja.add(n,  namemap.get(obj[0].toString()));
						ja_x.add(n, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(n, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						ja_z.add(n, sum3);
					}
					if(namemap.get(obj[0].toString())!=null&&!"".equals(namemap.get(obj[0].toString()))){
						n++;
					}
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("1")){
				
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				//funcItemId为父类的
				if("0".equals(org)){
					 statList2=accessDateDataService.getSumByTimeItem3A(start_time, end_time,funcItemId, "6", null, null, "ASC");
				}else{
					 statList2=accessDateDataService.getSumByTimeItem4A(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
				}
				//分栏目  -查询月的值
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				List<Object[]> list3=new ArrayList<Object[]>();
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							list3.add(list2.get(j));
						}
					}
				}
				//分栏目--月
				if("0".equals(funcItemId)){
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"2",org,funcItemId);
				}else{
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"2",org,funcItemId);
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				JSONArray jp = new JSONArray();
				//排序
				Collections.sort(statList, new Comparator<Object[]>() {
					public int compare(Object[] o1, Object[] o2) {
						int map1value = Integer.valueOf(o1[2].toString());
						int map2value = Integer.valueOf(o2[2].toString());
						return map2value - map1value;
					}
				});
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					if(i<5){
						ja.add(i, obj[0].toString());
						ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						ja_z.add(i, sum3);
					}
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("2")){
				
				//停留时间
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					statList2=accessDateDataService.getSumByTimeItem3A(start_time, end_time,funcItemId, "8", null, null, "ASC");
				}else{
					statList2=accessDateDataService.getSumByTimeItem4A(start_time, end_time,funcItemId, "8", null, null, "ASC",orgname);
				}
				List<Object[]> list2 = accessFunConfigService.findSubItems1(funcItemId);
				Map namemap=new HashMap();
				for(int i=0;i<list2.size();i++){
					namemap.put(list2.get(i)[0], list2.get(i)[1]);
				}
				//分栏目--日
				if("0".equals(funcItemId)){
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"1",org,funcItemId);
				}else{
					statList = accessDateIPService.getSumByItemss(start_time, end_time,"1",org,funcItemId);
				}
				for(int i=0;i<statList2.size();i++){
					for(int j=0;j<list2.size();j++){
						Object obj[]=statList2.get(i);
						if(statList2.get(i)[0].toString().equals(list2.get(j)[0].toString())){
							obj[0]=list2.get(j)[1].toString();
						}
					}
				}
				//排序
				Collections.sort(statList, new Comparator<Object[]>() {
					public int compare(Object[] o1, Object[] o2) {
						int map1value = Integer.valueOf(o1[2].toString());
						int map2value = Integer.valueOf(o2[2].toString());
						return map2value - map1value;
					}
				});
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				int n=0;
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if((statList2.get(j)[0]).equals(namemap.get(obj[0].toString()))){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
							
						}
					}
					if(namemap.get(obj[0].toString())==null||"".equals(namemap.get(obj[0].toString()))){
						continue;
					}
					jt.put("name", namemap.get(obj[0].toString()));
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(n, jt);
					if(n<5){
						ja.add(n,  namemap.get(obj[0].toString()));
						ja_x.add(n, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
						ja_y.add(n, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
						ja_z.add(n, sum3);
					}
					if(namemap.get(obj[0].toString())!=null&&!"".equals(namemap.get(obj[0].toString()))){
						n++;
					}
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if(statUnit.equals("3")){
				//分栏目  -查询月的值
				List<AccessFunConfig> list21 = accessFunConfigService.findItems(funcItemId);
				List<Object[]> statList2=new ArrayList<Object[]>();
				if("0".equals(org)){
					statList2=accessDateDataService.getSumByTimeItem2(start_time, end_time,funcItemId, "6", null, null, "ASC");
					statList = accessDateIPService.getSumByItems3(start_time, end_time,"2",org,funcItemId);
				}else{
					statList2=accessDateDataService.getSumByTimeItem22(start_time, end_time,funcItemId, "6", null, null, "ASC",orgname);
					statList = accessDateIPService.getSumByItems3(start_time, end_time,"2",org,funcItemId);
				}
				
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					if(list21.size()>0){
						name=list21.get(0).getFuncItemName();
					}
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].toString().equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i,sum3);
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else if (statUnit.equals("4")) {
				//分栏目--日
				List<AccessFunConfig> list21 = accessFunConfigService.findItems(funcItemId);
				if("0".equals(org)){
					statList = accessDateDataService.getSumByTimeItemB(start_time, end_time,funcItemId, timeType, null, null, "ASC");
				}else{
					statList = accessDateDataService.getSumByTimeItemA(start_time, end_time,funcItemId, timeType, null, null, "ASC",orgname);
				}
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					if(list21.size()>0){
						name=list21.get(0).getFuncItemName();
					}
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", Double.valueOf(obj[3].toString()));
					jp.add(i, jt);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, obj[3] == null ? 0 : (Double.valueOf(obj[3].toString())));
				}
				jo.put("name",  ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分时次
	 * @param start_time
	 * @param end_time
	 * @param statUnit 0时是按月查询
	 * @param statYear
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statProByTimeT")
	public String statProByTimeT(String start_time, String end_time, String statUnit,String funcItemId, String statYear, Integer page,
			Integer rows, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20160101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = "99999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			JSONArray ja_z = new JSONArray();
			//查询栏目名称 list
			List<AccessFunConfig> list = accessFunConfigService.findSubItems(funcItemId);
			if (statUnit == null || statUnit.equals("")) {
				//分栏目--日
				statList = accessDateDataService.getSumByTimeItem(start_time, end_time,funcItemId, timeType, null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					for(int j=0;j<list.size();j++){
						if(obj[0].toString().equals(list.get(j).getFuncItemId())){
							name=list.get(j).getFuncItemName();
						}
					}
					jt.put("name", name);
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", Double.valueOf(obj[3].toString()));
					jp.add(i, jt);
					ja.add(i, name);
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i, obj[3] == null ? 0 : (Double.valueOf(obj[3].toString())));
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}else{
				//分栏目  -查询月的值
				List<Object[]> statList2=accessDateDataService.getSumByTimeItem2(start_time, end_time,funcItemId, "6", null, null, "ASC");
				statList = accessDateDataService.getSumByTimeItem(start_time.substring(0,6), end_time.substring(0,6),funcItemId, "6", null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					String  name="";
					for(int j=0;j<list.size();j++){
						if(obj[0].toString().equals(list.get(j).getFuncItemId())){
							name=list.get(j).getFuncItemName();
						}
					}
					Double sum3=(double)0;
					for(int j=0;j<statList2.size();j++){
						if(obj[0].toString().equals(statList2.get(j)[0])){
							sum3=Double.valueOf(statList2.get(j)[1].toString());
						}
					}
					jt.put("name", name);
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jt.put("sum2", Integer.valueOf(obj[2].toString()));
					jt.put("sum3", sum3);
					jp.add(i, jt);
					ja.add(i, name);
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, obj[2] == null ? 0 : (Integer.valueOf(obj[2].toString())));
					ja_z.add(i,sum3);
				}
				jo.put("name", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				jo.put("sum3", ja_z);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo.toString());
				joall.put("list", jp);
				json = jo.toString();
				renderText(joall.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	
	@RequestMapping(value = "statClassByTime")
	public String getSumClassByTime(String start_time, String end_time, String statUnit, String statYear, Integer page,
			Integer rows, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		List<Object[]> accessinfoList;
		List<PageAccessCode> datadeflist;
		String json = "";
		try {
			if (start_time == null && end_time == null) {
				start_time = "0";
				end_time = "999999";
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "yyyymmDD";
			}
			// ===============统计下==============
			BigDecimal count = this.accessDateDataService.getTotalByTime(start_time, end_time);
			accessinfoList = accessDateDataService.getSumClassByTime(start_time, end_time, timeType, ordername, ordertype,
					null, null);
			datadeflist = pageAccessCodeService.getAllData();
			for (int i = 0; i < accessinfoList.size(); i++) {
				for (int j = 0; j < datadeflist.size(); j++) {
					if (accessinfoList.get(i).getPageId().toString().equals(datadeflist.get(j).getPageId().toString())) {
						accessinfoList.get(i).setPageName((datadeflist.get(j).getPageName()));
					}
				}
			}
			JSONObject jo_List = new JSONObject();
			JSONArray ja_List = new JSONArray();
			JSONArray ja_Picture = new JSONArray();

			for (int i = 0; i <= accessinfoList.size(); i++) {
				if (i == 0) {
					jo_List.put("pageName", "总计");
					jo_List.put("Sum", count + "");
					ja_List.add(i, jo_List);
				} else {
					jo_List.put("pageName", accessinfoList.get(i - 1).getPageName());
					jo_List.put("Sum", accessinfoList.get(i - 1).getSum() + "");
					ja_List.add(i, jo_List);
					JSONArray ja_temp = new JSONArray();
					ja_temp.add(0, accessinfoList.get(i - 1).getPageName() + "");
					ja_temp.add(1, accessinfoList.get(i - 1).getSum());
					ja_Picture.add(i - 1, ja_temp);
				}
			}
			JSONObject joTotal = new JSONObject();
			joTotal.put("list", ja_List);
			joTotal.put("picture", ja_Picture);
			json = joTotal.toString();
			System.out.println(json);
			renderText(json, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/

	/**
	 * 获取有记录的年份
	 * 
	 * @param start_time
	 * @param end_time
	 * @param statUnit 统计方式
	 * @param statYear
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statYears")
	public String statYears(String start_time, String end_time, String statUnit, String statYear,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		String json = "";
		JSONArray ja2 = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		List<String> list = accessDateDataService.statYears();
		jo.put("yearValue", 0 + "");
		jo.put("yearName", "请选择");
		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			jo.put("yearValue", list.get(i));
			jo.put("yearName", list.get(i) + "年");
			ja.add(i + 1, jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
	/**
	 * 获取有记录的年份
	 * 
	 * @param start_time
	 * @param end_time
	 * @param statUnit 统计方式
	 * @param statYear
	 * @param request
	 * @param response
	 * @param Request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getOrg")
	public String getOrg(String org,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		List<String> list = accessDateDataService.getOrg(org);
		jo.put("orgValue", 0 + "");
		jo.put("orgName", "全部");
		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			jo.put("orgValue", list.get(i));
			jo.put("orgName", list.get(i));
			ja.add(i + 1, jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
	@RequestMapping(value = "getOrgAll")
	public String getOrgAll(
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		List<String[]> list = accessDateDataService.getOrgAll();
		jo.put("orgValue", 0 + "");
		jo.put("orgName", "全部");
		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj=list.get(i);
			jo.put("orgValue", obj[1].toString());
			jo.put("orgName", obj[0].toString());
			ja.add(i + 1, jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
	@RequestMapping(value = "getFunction1")
	public String getFunction1(String pfuncItemId,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		if(pfuncItemId==null||"".equals(pfuncItemId)){
			pfuncItemId="0";
		}else if("-1".equals(pfuncItemId)){
			return null;
		}
		List<AccessFunConfig> list = accessFunConfigService.findSubItems(pfuncItemId);
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		jo.put("funcItemId", 0 + "");
		jo.put("funcItemName", "全部");
		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			jo.put("funcItemId", list.get(i).getFuncItemId());
			jo.put("funcItemName", list.get(i).getFuncItemName());
			ja.add(i + 1, jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
	@RequestMapping(value = "getFunctionB")
	public String getFunctionB(String pfuncItemId,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		if(pfuncItemId==null||"".equals(pfuncItemId)){
			pfuncItemId="702";
		}
		List<AccessFunConfig> list = accessFunConfigService.findSubItems(pfuncItemId);
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			jo.put("funcItemId", list.get(i).getFuncItemId());
			jo.put("funcItemName", list.get(i).getFuncItemName());
			ja.add(i , jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
	@RequestMapping(value = "getFunction2")
	public String getFunction2(String pfuncItemId,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		if(pfuncItemId==null||"".equals(pfuncItemId)){
			pfuncItemId="0";
		}else if("0".equals(pfuncItemId)){
			return null;
		}
		List<AccessFunConfig> list = accessFunConfigService.findSubItems(pfuncItemId);
		String json = "";
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
//		jo.put("funcItemId", 0 + "");
//		jo.put("funcItemName", "全部");
//		ja.add(0, jo);
		for (int i = 0; i < list.size(); i++) {
			jo.put("funcItemId", list.get(i).getFuncItemId());
			jo.put("funcItemName", list.get(i).getFuncItemName());
			ja.add(i, jo);
		}
		json = ja.toString();
		renderText(json, response);
		return null;
	}
}
