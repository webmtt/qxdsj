package com.thinkgem.jeesite.modules.statistics.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.Users.service.OrgInfoService;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import com.thinkgem.jeesite.modules.statistics.service.UserInfoService2;
import com.thinkgem.jeesite.modules.sys.entity.PageAccessCode;

/**
 * 
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-12-20
 */
@Controller
@RequestMapping(value = "statistics/statisticsUserInfo")
public class StatisticsUserInfoController extends BaseController {
	protected BigDecimal unit_TB = new BigDecimal(1);
	protected List<PageAccessCode> datadeflist;
	private String timeType, ordername, ordertype;
	@Autowired
	private OrgInfoService orgInfoService;
	@Autowired
	private UserInfoService2 userInfoService;
	
	@RequestMapping(value = "statisticsUserInfoTotalList")
	public String statisticsAccessInfoTotalList(Model model) {
		return "modules/statistics/statisticsUserInfoTotal";
	}
	@RequestMapping(value = "statisticsUserInfoProList")
	public String statisticsAccessInfoProList(Model model) {
		return "modules/statistics/statisticsUserInfoPro";
	}
	@RequestMapping(value = "statisticsUserInfoOrgList")
	public String statisticsAccessInfoOrgList(Model model) {
		return "modules/statistics/statisticsUserInfoOrg";
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
			String statUnit, String statYear, HttpServletResponse response, HttpServletRequest Request, Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		List<Object[]> statList;
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd"); 
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20150101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = df.format(new Date());
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//总量 逐月
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
				statList = userInfoService.getSumByTime(start_time, end_time, timeType, null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("3")) {
				statList = userInfoService.getSumByTime(start_time, end_time, "8", null, null, "ASC");
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString());
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("2")) {
				//逐月
				 Calendar   cal_1=Calendar.getInstance();
				 cal_1.setTime(df.parse(end_time+"01"));
				 cal_1.roll(Calendar.DATE, -1);
				statList = userInfoService.getSumByTime(start_time + "01", df.format(cal_1.getTime()), "6", null, null, "ASC");
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString().substring(0, 4)+"年"+obj[0].toString().substring(4, 6)+"月");
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
				}
				jo.put("time", ja);
				jo.put("sum", ja_x);
				jo.put("sum2", ja_y);
				json = jo.toString();
				renderText(json, response);
			}else if (statUnit.equals("1")) {
				//逐年
				statList = userInfoService.getSumByTime(start_time+ "0101", end_time+ "1231", "4", null, null, "ASC");
				for (int i = 0; i < statList.size(); i++) {
					Object[] obj=statList.get(i);
					ja.add(i, obj[0].toString().substring(0, 4)+"年");
					ja_x.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
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
			Integer page, Integer rows, String statYear, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		String json = "";
		List<Object[]> statList;
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd"); 
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20150101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = df.format(new Date());
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			page = (page == null ? 1 : page);
			rows = (rows == null ? 10 : rows);
			statUnit = (statUnit == null ? "" : statUnit);
			if (statUnit == null || statUnit.equals("")) {
				List<Object[]> statList1 = userInfoService.getSumByTime(start_time, end_time,timeType, null, null, null);
				statList = userInfoService.getSumByTime(start_time, end_time, timeType, page, rows, null);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString());
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja.add(i, jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json, response);
			}  else if (statUnit.equals("3")) {
				List<Object[]> statList1 = userInfoService.getSumByTime(start_time, end_time,"8", null, null, null);
				statList = userInfoService.getSumByTime(start_time, end_time, "8", page, rows, null);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString());
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja.add(i, jo);
				}
				JSONObject jobject = new JSONObject();
				jobject.put("total",statList1.size());
				jobject.put("rows", ja);
				json = jobject.toString();
				renderText(json, response);
			}else if (statUnit.equals("2")) {
				 Calendar   cal_1=Calendar.getInstance();
				 cal_1.setTime(df.parse(end_time+"01"));
				 cal_1.roll(Calendar.DATE, -1);
				statList = userInfoService.getSumByTime(start_time + "01", df.format(cal_1.getTime()), "6", null, null, null);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString());
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja.add(i, jo);
				}
				renderText(ja.toString(), response);
			}else if (statUnit.equals("1")) {
				statList = userInfoService.getSumByTime(start_time + "0101", end_time+ "1231", "4", null, null, null);
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					jo.put("time",obj[0].toString().substring(0, 4)+"年");
					jo.put("sum",obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja.add(i, jo);
				}
				renderText(ja.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

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
			Integer rows, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd"); 
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20150101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = df.format(new Date());
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
				statList = userInfoService.getSumByTimeOrg(start_time, end_time, timeType, null, null, "DESC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					JSONArray pj = new JSONArray();
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jp.add(i, jt);
					pj.add(0, obj[0].toString());
					pj.add(1, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, pj);
				}
				JSONObject joall = new JSONObject();
				joall.put("pic", ja_y);
				joall.put("list", jp);
				renderText(joall.toString(), response);
			}else if("3".equals(statUnit)){
				 Calendar   cal_1=Calendar.getInstance();
				 cal_1.setTime(df.parse(start_time));
				 cal_1.roll(Calendar.DATE, -1);
				statList = userInfoService.getSumByTimeOrg(start_time, df.format(cal_1.getTime()), "8", null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					JSONArray pj = new JSONArray();
					jt.put("name", obj[0].toString());
					jt.put("sum", Integer.valueOf(obj[1].toString()));
					jp.add(i, jt);
					pj.add(0, obj[0].toString());
					pj.add(1, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					ja_y.add(i, pj);
				}
				JSONObject joall = new JSONObject();
				joall.put("pic", ja_y);
				joall.put("list", jp);
				renderText(joall.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分省
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
	@RequestMapping(value = "statProByTime")
	public String statProByTime(String start_time, String end_time, String statUnit, String statYear, Integer page,
			Integer rows, HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request,
			Model model) {
		JSONArray ja = new JSONArray();
		String json = "";
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd"); 
		List<Object[]> statList;
		try {
			if (start_time == null || start_time.equals("")) {
				start_time = "20150101";
			}
			if (end_time == null || end_time.equals("")) {
				end_time = df.format(new Date());
			}
			if (timeType == null || timeType.equals("")) {
				timeType = "8";
			}
			//一天或者一个月的统计情况 start_time和end_time相同
			JSONObject jo = new JSONObject();
			JSONArray ja_x = new JSONArray();
			JSONArray ja_y = new JSONArray();
			if (statUnit == null || statUnit.equals("")) {
				statList = userInfoService.getSumByTimePro(start_time, end_time, timeType, null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					JSONArray pj = new JSONArray();
					jt.put("name", obj[0].toString().replace("气象局", ""));
					jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jp.add(i, jt);
					ja_x.add(i, obj[0].toString().replace("气象局", ""));
					ja_y.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
				}
				jo.put("name", ja_x);
				jo.put("sum", ja_y);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo);
				joall.put("list", jp);
				renderText(joall.toString(), response);
			}else if("3".equals(statUnit)){
				 Calendar   cal_1=Calendar.getInstance();
				 cal_1.setTime(df.parse(start_time));
				 cal_1.roll(Calendar.DATE, -1);
				statList = userInfoService.getSumByTimePro(start_time, df.format(cal_1.getTime()), "8", null, null, "ASC");
				//ja_x -- ip   ja_y -- pv
				JSONArray jp = new JSONArray();
				for (int i = 0; i <= statList.size()-1; i++) {
					Object[] obj=statList.get(i);
					JSONObject jt = new JSONObject();
					JSONArray pj = new JSONArray();
					jt.put("name", obj[0].toString().replace("气象局", ""));
					jt.put("sum", obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
					jp.add(i, jt);
					ja_x.add(i, obj[0].toString().replace("气象局", ""));
					ja_y.add(i, obj[1] == null ? 0 : (Integer.valueOf(obj[1].toString())));
				}
				jo.put("name", ja_x);
				jo.put("sum", ja_y);
				JSONObject joall = new JSONObject();
				joall.put("pic", jo);
				joall.put("list", jp);
				renderText(joall.toString(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@RequestMapping(value = "statYears")
	public String statYears(String start_time, String end_time, String statUnit, String statYear,
			HttpServletRequest request, HttpServletResponse response, HttpServletRequest Request, Model model) {
		String json = "";
		JSONArray ja2 = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		List<String> list = userInfoService.statYears();
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
	
}
