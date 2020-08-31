package com.thinkgem.jeesite.modules.recordquery.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlData;
import com.thinkgem.jeesite.modules.recordquery.service.FtpUrlDataService;

@Controller
@RequestMapping(value = "ftpUrlData/")
public class FtpDataController extends BaseController{
	@Resource
	private FtpUrlDataService fService;
	@RequestMapping("/datasizeAndNumByData")
	public void getDatasizeAndNumByData(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String begin=(String)paramMap.get("begin");
		String end=(String)paramMap.get("end");
		List<Map<String,String>> list=fService.getdatasizeandpvNumber(begin, end);
		Map map=new HashMap();
		map.put("list", list);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping("/datasizeAndNumByData2")
	public void getDatasizeAndNumByData2(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String begin=(String)paramMap.get("begin");
		String end=(String)paramMap.get("end");
		List<Map<String,String>> list=fService.getdatasizeandpvNumber2(begin, end);
		Map map=new HashMap();
		map.put("list", list);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping("/datasizeAndNumByDept")
	public void getDatasizeAndNumByDept(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String begin=(String)paramMap.get("begin");
		String end=(String)paramMap.get("end");
		List<Map<String,String>> list=fService.getdatasizeandpvNumberByDept(begin, end);
		Map map=new HashMap();
		map.put("list", list);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping("/flist")
	public String getNoticeList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String sortds=request.getParameter("sortds");
		String sortad=request.getParameter("sortad");
		if("".equals(sortds)||sortds==null||"".equals(sortad)||sortad==null){
			sortds="dsd";
			sortad="add";
		}
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");	
		String begin=(String)paramMap.get("begin");
		if(null!=begin&&!"".equals(begin)){
		model.addAttribute("begin", begin);
		}else{
			Calendar c=Calendar.getInstance();
			c.setTime(c.getTime());
			c.add(Calendar.DAY_OF_MONTH,-7); 
			begin=sf.format(c.getTime());
		}
		String end=(String)paramMap.get("end");
		if(null!=end&&!"".equals(end)){
			model.addAttribute("end",end);
		}else{
			Calendar c=Calendar.getInstance();
			end=sf.format(c.getTime());
		}
		Page<FtpUrlData> notices=fService.getDataByPage( new Page(request,response),begin,end,sortds,sortad);
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("page",notices);
		model.addAttribute("sortds",sortds);
		model.addAttribute("sortad",sortad);
		return "modules/UserBehavior/FtpUrlDataList";		
	}
	/**
	 * 根据选择查询的类型（今天1，昨天2，过去一周3，过去一月4），得出查询的开始时间和结束时间
	 * @param type
	 * @return
	 */
	public String[] getDate(String type){
		Calendar c=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String end="";
		String begin="";
		String[] date=new String[2];
		Date d=new Date();
		if("1".equals(type)){
			end=sdf.format(d);
			begin=end;
		}
		if("2".equals(type)){
			c.add(Calendar.DAY_OF_MONTH, -1);
			end=sdf.format(c.getTime());
			begin=end;
		}
		if("3".equals(type)){
			c.add(Calendar.DAY_OF_MONTH, -1);
			end=sdf.format(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -6);
			begin=sdf.format(c.getTime());
		}
		if("4".equals(type)){
			c.add(Calendar.DAY_OF_MONTH, -1);
			end=sdf.format(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -29);
			begin=sdf.format(c.getTime());
		}
		if("5".equals(type)){
			c.add(Calendar.DAY_OF_MONTH, -1);
			end=sdf.format(c.getTime());
			c.add(Calendar.YEAR, -1);
			begin=sdf.format(c.getTime());
		}
		date[0]=begin;
		date[1]=end;
		return date;
	}
	@RequestMapping("/downSize")
	public void getDownSize(HttpServletRequest request,HttpServletResponse response){
		String type=request.getParameter("type");
		String showType=request.getParameter("showType");
		String[] s=getDate(type);
		List list=fService.getDownSize(s[0],s[1],showType);
		String json=JsonMapper.toJsonString(list);
		renderText(json, response);
	}
	@RequestMapping("/pvNumber")
	public void getPvNumber(HttpServletRequest request,HttpServletResponse response){
		String type=request.getParameter("type");
		String showType=request.getParameter("showType");
		String[] s=getDate(type);
		List list=fService.getPvNumber(s[0],s[1],showType);
		String json=JsonMapper.toJsonString(list);
		renderText(json, response);
	}
}
