package com.thinkgem.jeesite.modules.statistics.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.ConnOrder;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.portal.dao.DbSqlDao;
import com.thinkgem.jeesite.modules.portal.entity.DsDb;
import com.thinkgem.jeesite.modules.portal.entity.DsPro;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2018-06-27
 */
@Controller
@RequestMapping(value = "statistics/statisticsCimissAccess")
public class StatisticsCimissAccessController extends BaseController {
	@Autowired
	private ComparasDao comparasDao;
	@Autowired
	private DbSqlDao dbSqlDao;
	
	@RequestMapping(value = "statisticsView")
	public String statisticsView(Model model,HttpServletRequest request, HttpServletResponse response){
		return "modules/statistics/statisticsCimissAccessTotal";
	}
	
	@RequestMapping(value = "statisticsTotalList")
	public void statisticsTotalList(String level, String sort, String startTime1,String endTime1, String startTime2, String endTime2, String num,Model model,HttpServletRequest request, HttpServletResponse response) {
		//String url = "http://10.20.76.31:8008/musicLogStat/logStat?userId=test&pwd=test&datacode=OTHE_MUSIC_SERV_INFO_DAY&timeRange=[20150601000000,20160602000000]&interfaceId=statServiceInfoByTimeRange";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMM");
		
		String startTimeValue = null;
		String EndTimeValue = null;
		String startTimeValue2 = null;
		String EndTimeValue2 = null;
		try {
			if("0".equals(num)){
				//年
				startTimeValue = sdf.format(sdf4.parse(startTime1));
				EndTimeValue = sdf.format(sdf4.parse(endTime1));
				startTimeValue2 = sdf.format(sdf4.parse(startTime2));
				EndTimeValue2 = sdf.format(sdf4.parse(endTime2));
			}else if("1".equals(num)){
				//月
				startTimeValue = sdf5.format(sdf3.parse(startTime1));
				EndTimeValue = sdf5.format(sdf3.parse(endTime1));
				startTimeValue2 = sdf5.format(sdf3.parse(startTime2));
				EndTimeValue2 = sdf5.format(sdf3.parse(endTime2));
			}else if("2".equals(num)){
				//日
				startTimeValue = sdf.format(sdf2.parse(startTime1));
				EndTimeValue = sdf.format(sdf2.parse(endTime1));
				startTimeValue2 = sdf.format(sdf2.parse(startTime2));
				EndTimeValue2 = sdf.format(sdf2.parse(endTime2));
			}
			
		if("1".equals(num)){
			if(Integer.parseInt(level)==1&&Integer.parseInt(sort)==1){
				List<Map> cimisspie = getCimisspie("flood_cimiss_org_nation_mon", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_org_nation_mon", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}else if(Integer.parseInt(level)==1&&Integer.parseInt(sort)==2){
				List<Map> cimisspie = getCimisspie("flood_cimiss_data_nation_mon", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_data_nation_mon", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}
		}else if("2".equals(num)){
			if(Integer.parseInt(level)==1&&Integer.parseInt(sort)==1){
				List<Map> cimisspie = getCimisspie("flood_cimiss_org_nation", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_org_nation", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}else if(Integer.parseInt(level)==1&&Integer.parseInt(sort)==2){
				List<Map> cimisspie = getCimisspie("flood_cimiss_data_pro", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_data_pro", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}else if(Integer.parseInt(level)==2&&Integer.parseInt(sort)==1){
				List<Map> cimisspie = getCimisspie("flood_cimiss_org_pro", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_org_pro", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}else if(Integer.parseInt(level)==2&&Integer.parseInt(sort)==2){
				List<Map> cimisspie = getCimisspie("flood_cimiss_data_nation", startTimeValue, EndTimeValue);
				List<Map> cimisspie2 = getCimisspie("flood_cimiss_data_nation", startTimeValue2, EndTimeValue2);
				Map dataMap = getDataMap(cimisspie, cimisspie2);
				String jsonString = JsonMapper.toJsonString(dataMap);
				renderText(jsonString, response);
			}
		}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
    
	@RequestMapping(value = "statisticsCimiss")
	public void statisticsCimiss(String stime,String etime,HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");
		String stime1 = null;
		String etime1 = null;
		try {
			stime1 = sdf.format(sdf2.parse(stime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			etime1 = sdf.format(sdf2.parse(etime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map> cimisspie = getCimisspie("flood_cimiss_data_nation_hour", stime1, etime1);
		String jsonString = JsonMapper.toJsonString(cimisspie);
		renderText(jsonString, response);
	}
	
	@RequestMapping(value = "/exporExcel")
	public void exporExcel(String jsonStr,HttpServletRequest request, HttpServletResponse response) {
		try {
			jsonStr=URLDecoder.decode(jsonStr,"UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(jsonStr);
		InputStream inp = null;
		try {
			inp = new FileInputStream(SpringContextHolder.getRootRealPath()+"/StatisticsCimissTemplate.xls");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Workbook wb = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(inp);
			wb = new HSSFWorkbook(fs);
			Sheet sheet= wb.getSheetAt(0);    //获取第一个sheet页
			int cellNums=sheet.getRow(0).getLastCellNum(); //获取列数
			int rowIndex=1;
			JSONArray jsonArray = JSONArray.fromObject(jsonStr);
			for (int i = 0; i < jsonArray.size(); i++) {
				Row row=sheet.createRow(rowIndex++);
				JSONObject dataJson = JSONObject.fromObject(jsonArray.get(i));
				row.createCell(0).setCellValue(dataJson.get("name").toString());
				row.createCell(1).setCellValue(dataJson.get("num1").toString());
				row.createCell(2).setCellValue(dataJson.get("num2").toString());
				row.createCell(3).setCellValue(dataJson.get("datasize1").toString());
				row.createCell(4).setCellValue(dataJson.get("datasize2").toString());
			}
			export(response, wb, "CIMISS访问量统计.xls");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void export(HttpServletResponse response,Workbook wb,String fileName)throws Exception{
		response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"iso8859-1"));
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		OutputStream out=response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}
	
	public List<Map> getCimisspie(String key,String startTime,String endTime){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		DsPro dsPro=dbSqlDao.getDsPro(key);
		DsDb dsdb = dsPro.getDsdb();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnOrder connOrder=new ConnOrder();
		conn = connOrder.getConn(dsdb.getDbtype(), dsdb.getDblink(), dsdb.getUsername(), dsdb.getPassword());
		Map<String,String> param=new HashMap<String,String>();
		param.put("startTime",startTime);
		param.put("endTime",endTime);
		String sql=dsPro.getProsql();
		for(String name:param.keySet()){
			sql=sql.replaceAll(":"+name, param.get(name));
		}
		List<Map> list = new ArrayList();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		if(conn!=null){
			try {
				ps = conn.prepareStatement(sql);
				ps.executeQuery();
				rs = ps.getResultSet();
				while(rs.next()){
					Map map = new HashMap();
					map.put("name", rs.getObject(1));
					map.put("num", rs.getInt(2));
					map.put("datasize",df.format(rs.getFloat(3)));
					list.add(map);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(rs!=null){
						rs.close();
					}
					if(ps!=null){
						ps.close();
					}
					if(conn!=null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		return list;
	}
	
	public Map getDataMap(List<Map> cimisspie,List<Map> cimisspie2){
		List<String> namelist = new ArrayList<String>(); 
		List<List> numList1 = new ArrayList<List>(); 
		List<List> numList2 = new ArrayList<List>(); 
		List<List> datasizeList1 = new ArrayList<List>();
		List<List> datasizeList2 = new ArrayList<List>();
		List<Map> tableList = new ArrayList<Map>();
		for (Map map1 : cimisspie) {
			for (Map map2 : cimisspie2) {
				if(map2.get("name").equals(map1.get("name"))){
					Map map = new HashMap();
					Map numColomnMap = new HashMap();
					Map datasizeColomnMap = new HashMap();
					List list1 = new ArrayList();
					List list2 = new ArrayList();
					List list3 = new ArrayList();
					List list4 = new ArrayList();
					list1.add((String) map1.get("name"));
					list2.add((String) map1.get("name"));
					list3.add((String) map1.get("name"));
					list4.add((String) map1.get("name"));
					list1.add((Integer)map1.get("num"));
					list2.add((Integer)map2.get("num"));
					list3.add(Double.parseDouble((String) map1.get("datasize")));
					list4.add(Double.parseDouble((String) map2.get("datasize")));
					namelist.add((String) map1.get("name"));
					numList1.add(list1);
					numList2.add(list2);
					datasizeList1.add(list3);
					datasizeList2.add(list4);
					
					
					map.put("name", map1.get("name"));
					map.put("num1", map1.get("num"));
					map.put("num2", map2.get("num"));
					map.put("datasize1", map1.get("datasize"));
					map.put("datasize2", map2.get("datasize"));
					tableList.add(map);
				}
			}
		}
		Map dataMap = new HashMap();
		dataMap.put("namelist", namelist);
		dataMap.put("numList1", numList1);
		dataMap.put("numList2", numList2);
		dataMap.put("datasizeList1", datasizeList1);
		dataMap.put("datasizeList2", datasizeList2);
		dataMap.put("tableList", tableList);
		return dataMap;
	}
	
	/*String url = comparasDao.getComparasByKey("CimissAccessSearchUrl").toString();
	try {
		String startTimeValue = sdf2.format(sdf2.parse(startTime));
		String EndTimeValue = sdf2.format(sdf2.parse(endTime));
		url.replace("startTime", startTimeValue);
		url.replace("endTime", EndTimeValue);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	String json = ConPostUtils.readContentFromHttpGet(url, new ArrayList<NameValuePair>());*/
}
