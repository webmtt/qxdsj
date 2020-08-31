package com.thinkgem.jeesite.modules.recordquery.web;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlConf;
import com.thinkgem.jeesite.modules.recordquery.service.FtpUrlConfService;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;

import us.monoid.json.JSONObject;
import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

@Controller
@RequestMapping(value = "ftpUrlData/")
public class FtpDownLoadController extends BaseController{
	@Resource
	private FtpUrlConfService fService;
	
	@Resource
	private ComparasService comService;
	
	@RequestMapping("/getDataByCode")
	public void getDataByCode(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String beginTime=request.getParameter("starDate");
		String endTime=request.getParameter("endDate");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
		long begin=0;
		long end=0;
		try {
			begin=sdf.parse(beginTime).getTime();
			end=sdf.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Map> mapList=new ArrayList<Map>();
		DecimalFormat df = new DecimalFormat("0.00");
		List<FtpUrlConf> list=fService.findAll();
		if(list!=null&&list.size()>0){
			for(FtpUrlConf ftp:list){
				String query=ftp.getMatchUrl();
				String urlKey=ftp.getGetDataUrl();
				String queryUrl=comService.getComparas(urlKey).getStringvalue();
				if(query!=null&&!"".equals(query)){ 
					String queryString="{\"size\": 0,\"aggs\": {\"2\": {\"sum\": {\"field\": \"fileSize\"}},\"3\":"
							+ " {\"cardinality\": {\"field\": \"remoteHost\"}}},\"query\": {\"filtered\": "
							+ "{\"query\": {\"query_string\": {\"query\": \""+query+"\","+
							    "\"analyze_wildcard\": true}},\"filter\": {\"bool\": {\"must\": [{\"range\": "
							    + "{\"@timestamp\": {\"gte\": "+begin+",\"lte\": "+end+
							    "}}}],\"must_not\": []}}}}}";
					Content content = new Content("", queryString.getBytes());
					Resty r = new Resty();
					try {
						JSONResource data = r.json(queryUrl, content);
						JSONObject hits = (JSONObject) data.get("hits");
						JSONObject aggregations = (JSONObject) data.get("aggregations");
						JSONObject two=(JSONObject) aggregations.get("2");
						JSONObject three=(JSONObject) aggregations.get("3");
						String total=hits.getString("total");
						String twoValue=two.getString("value");
						String ipNum=three.getString("value");
						double d=Double.parseDouble(twoValue);
						if(d!=0){
							Map map=new HashMap();
							map.put("datasize",df.format(d/1024/1024/1024));
							map.put("num", Integer.parseInt(total));
							map.put("name",ftp.getChnName());
							mapList.add(map);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		jsonMap.put("list", mapList);
		renderText(JsonMapper.toJsonString(jsonMap), response);
	}
	@RequestMapping("/getDataByUnit")
	public void getDataByUnit(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String beginTime=request.getParameter("starDate");
		String endTime=request.getParameter("endDate");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
		long begin=0;
		long end=0;
		try {
			begin=sdf.parse(beginTime).getTime();
			end=sdf.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
