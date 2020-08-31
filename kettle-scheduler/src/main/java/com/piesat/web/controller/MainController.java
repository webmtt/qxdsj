package com.piesat.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.piesat.core.model.KCategory;
import com.piesat.core.model.KTrans;
import com.piesat.core.model.KTransRecord;
import com.piesat.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.common.toolkit.Constant;
import com.piesat.core.dto.BootTablePage;
import com.piesat.core.dto.ResultDto;
import com.piesat.core.model.KUser;
import com.piesat.web.utils.JsonUtils;

@RestController
@RequestMapping("/main/")
public class MainController {

	@Autowired
	private TransMonitorService transMonitorService;
	
	@Autowired
	private JobMonitorService jobMonitorService;

	@Autowired
	private TransRecordService transRecordService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TransService transService;
	
	/**
	 * @Title allRuning
	 * @Description 
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("allRuning.shtml")
	public String allRuning(HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		Integer allMonitorTrans = transMonitorService.getAllMonitorTrans(kUser.getuId());
		Integer allMonitorJob = jobMonitorService.getAllMonitorJob(kUser.getuId());
		Integer allRuning = allMonitorTrans + allMonitorJob; 
		return JsonUtils.objectToJson(allRuning);
	}
	
	/**
	 * @Title getTransList
	 * @Description 获取转换的Top5
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("getTransList.shtml")
	public String getTransList(HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		BootTablePage list = transMonitorService.getList(kUser.getuId());
		return JsonUtils.objectToJson(list);
	}
	
	/**
	 * @Title getJobList
	 * @Description 获取作业的Top5
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("getJobList.shtml")
	public String getJobList(HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		BootTablePage list = jobMonitorService.getList(kUser.getuId());
		return JsonUtils.objectToJson(list);
	}
	
	/**
	 * 
	 * @Title getKettleLine
	 * @Description TODO
	 * @return
	 * @return String
	 * @throws ParseException 
	 */
	@RequestMapping("getKettleLine.shtml")
	public String getKettleLine(HttpServletRequest request){		
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();
		for (int i = -6; i <= 0; i++){
			Calendar instance = Calendar.getInstance();
			instance.add(Calendar.DATE, i);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateFormat = simpleDateFormat.format(instance.getTime());
			dateList.add(dateFormat);
		}
		resultMap.put("legend", dateList);
		Map<String, Object> transLine = transMonitorService.getTransLine(kUser.getuId());
		resultMap.put("trans", transLine);
		Map<String, Object> jobLine = jobMonitorService.getJobLine(kUser.getuId());
		resultMap.put("job", jobLine);
		return ResultDto.success(resultMap);
	}

	@RequestMapping("getPhysics.shtml")
	public String getPhysics(String standbe){
		String[] times = standbe.split(",");
		String startTime = times[0];
		String endTime = times[1];
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> baseMap = new HashMap<String, Object>();
		Map<String,Object> fileMap = new HashMap<String, Object>();
		Map<String,Object> apiMap = new HashMap<String, Object>();
		List<KCategory> calist = categoryService.getAll();
		for (KCategory kcat:calist) {
			int categoryId = kcat.getCategoryId();
			String categoryName = kcat.getCategoryName();
			if(categoryName.contains("库表")){
				List<KTrans> transListBase = transService.getByID(categoryId);
				List<KTransRecord> list = new ArrayList<>();
				for (KTrans kTrans1:transListBase) {
					int kTransId = kTrans1.getTransId();
					String TransName = kTrans1.getTransName();
					list = transRecordService.getAll(kTransId,startTime,endTime);
					List<Integer> numlist = new ArrayList<>();
					List<String> timelist = new ArrayList<>();
					for (KTransRecord kTransRecord:list) {
						timelist.add(simpleDateFormat.format(kTransRecord.getStopTime()));
						numlist.add(kTransRecord.getRecordId());
					}
					if(TransName.contains("同步")){
						baseMap.put("synchronize",numlist);
						baseMap.put("synchronizetime",timelist);
					}else{
						baseMap.put("change",numlist);
						baseMap.put("changetime",timelist);
					}
				}
			}else if(categoryName.contains("文件")){
				List<KTrans> transListBase = transService.getByID(categoryId);
				List<KTransRecord> list = new ArrayList<>();
				for (KTrans kTrans1:transListBase) {
					int kTransId = kTrans1.getTransId();
					list = transRecordService.getAll(kTransId,startTime,endTime);
					List<Integer> numlist = new ArrayList<>();
					List<String> timelist = new ArrayList<>();
					for (KTransRecord kTransRecord:list) {
						timelist.add(simpleDateFormat.format(kTransRecord.getStopTime()));
						numlist.add(kTransRecord.getRecordId());
					}
					if(kTrans1.getTransName().contains("同步")){
						fileMap.put("synchronize",numlist);
						fileMap.put("synchronizetime",timelist);
					}else{
						fileMap.put("change",numlist);
						fileMap.put("changetime",timelist);
					}
				}
			}else if(categoryName.contains("接口")){
				List<KTrans> transListBase = transService.getByID(categoryId);
				List<KTransRecord> list = new ArrayList<>();
				for (KTrans kTrans1:transListBase) {
					int kTransId = kTrans1.getTransId();
					list = transRecordService.getAll(kTransId,startTime,endTime);
					List<Integer> numlist = new ArrayList<>();
					List<String> timelist = new ArrayList<>();
					for (KTransRecord kTransRecord:list) {
						timelist.add(simpleDateFormat.format(kTransRecord.getStopTime()));
						numlist.add(kTransRecord.getRecordId());
					}
					if(kTrans1.getTransName().contains("同步")){
						apiMap.put("synchronize",numlist);
						apiMap.put("synchronizetime",timelist);
					}else{
						apiMap.put("change",numlist);
						apiMap.put("changetime",timelist);
					}
				}
			}
		}
		resultMap.put("apiMap",apiMap);
		resultMap.put("fileMap",fileMap);
		resultMap.put("baseMap",baseMap);
		return ResultDto.success(resultMap);
	}
}
