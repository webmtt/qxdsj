package com.thinkgem.jeesite.modules.recordquery.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.recordquery.entity.DataDef1;
import com.thinkgem.jeesite.modules.recordquery.entity.DataSearchDef;
import com.thinkgem.jeesite.modules.recordquery.entity.OrderAccessInfoModel;
import com.thinkgem.jeesite.modules.recordquery.service.DataSearchDefService;
import com.thinkgem.jeesite.modules.recordquery.service.DatadefService;
import com.thinkgem.jeesite.modules.recordquery.service.OrderAccessInfoService;

import net.sf.json.JSONArray;

/**
 * 用户Controller
 */ 
@Controller
@RequestMapping(value = "orderData/")
public class StatisticsOrderController extends BaseController {
	@Autowired
	private OrderAccessInfoService orderAccessInfoService;
	@Autowired
	private DatadefService datadefService;
	@Autowired
	private DataSearchDefService dataSearchDefService;
	protected BigDecimal unit_TB = new BigDecimal(1);
	
	@RequestMapping(value = "/orderCount")
	public String orderCount(HttpServletRequest request,HttpServletResponse response, Model model) {
		String type=request.getParameter("type");
		String[] s=getDate(type);
		String start_time=s[0];
		String end_time=s[1];
		 String json = "";
		 List<OrderAccessInfoModel> statList;
		 List<DataDef1> datadeflist;
		try {
			//根据下载量排序，根据资料分组
			statList = this.orderAccessInfoService.getSumClassByTime(start_time, end_time);
			datadeflist = datadefService.findAll();
			for(int i=0;i<statList.size();i++){
				if(statList.get(i).getDatacode()!=null){
					String chnName=dataSearchDefService.findByCode(statList.get(i).getDatacode()).getDataChnname();
					if(chnName!=null&&!"".equals(chnName)){
						statList.get(i).setTitle(chnName);
					}else{
						for(int j=0;j<datadeflist.size();j++){
							if(statList.get(i).getDatacode()!=null){
								if(statList.get(i).getDatacode().equals(datadeflist.get(j).getDatacode())){
									statList.get(i).setTitle(datadeflist.get(j).getChnname());
									break;
								}					
							}			
						}
					}
				}
			}
			JSONArray ja_Picture = new JSONArray();	
			JSONArray ja_temp = new JSONArray();
			JSONArray ja_count = new JSONArray();
			for(int i=0;i<statList.size();i++){
				if(statList.get(i).getDatacode()!=null&&!statList.get(i).getDatacode().equals("null")){
					if(statList.get(i).getTitle()!=null){
						ja_temp.add(i, statList.get(i).getTitle());
					}else {
						ja_temp.add(i, statList.get(i).getDatacode()+"");
					}
					if(statList.get(i).getNumber()!=null){
						ja_count.add(i, new  BigDecimal(statList.get(i).getNumber()));
					}
					
					}
			}
			ja_Picture.add(0,ja_temp);
			ja_Picture.add(1,ja_count);
			json = ja_Picture.toString();
			 renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}
	@RequestMapping(value = "/orderSize")
	public String orderSize(HttpServletRequest request,HttpServletResponse response, Model model) {
		String type=request.getParameter("type");
		String[] s=getDate(type);
		String start_time=s[0];
		String end_time=s[1];
		String json = "";
		 List<OrderAccessInfoModel> statList;
		 List<DataDef1> datadeflist;
		try {
			//根据下载量排序，根据资料分组
			statList = this.orderAccessInfoService.getDownLoadSumClassByTime(start_time, end_time);
			datadeflist = datadefService.findAll();
			for(int i=0;i<statList.size();i++){
				if(statList.get(i).getDatacode()!=null){
					String chnName=dataSearchDefService.findByCode(statList.get(i).getDatacode()).getDataChnname();
					if(chnName!=null&&!"".equals(chnName)){
						statList.get(i).setTitle(chnName);
					}else{
						for(int j=0;j<datadeflist.size();j++){
							if(statList.get(i).getDatacode()!=null){
								if(statList.get(i).getDatacode().equals(datadeflist.get(j).getDatacode())){
									statList.get(i).setTitle(datadeflist.get(j).getChnname());
									break;
								}					
							}			
						}
					}
				}
			}
			JSONArray ja_Picture = new JSONArray();	
			JSONArray ja_temp = new JSONArray();
			JSONArray ja_count = new JSONArray();
			for(int i=0;i<statList.size();i++){
				if(statList.get(i).getDatacode()!=null&&!statList.get(i).getDatacode().equals("null")){
					if(statList.get(i).getTitle()!=null){
						ja_temp.add(i, statList.get(i).getTitle());
					}else {
						ja_temp.add(i, statList.get(i).getDatacode()+"");
					}
					Double num=statList.get(i).getDownNumber();
					if(num==0){
						num=0.01;
					}
					ja_count.add(i,num);
				}
			}
			 ja_Picture.add(0,ja_temp);
			 ja_Picture.add(1,ja_count);
			 json = ja_Picture.toString();
			 renderText(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
}
