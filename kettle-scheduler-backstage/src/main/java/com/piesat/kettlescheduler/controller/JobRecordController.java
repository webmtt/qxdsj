package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.BootTablePage;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.JobRecordService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/job/record/")
public class JobRecordController {

	@Autowired
	private JobRecordService jobRecordService;
	
	@RequestMapping("getList.shtml")
	public String getList(Integer offset, Integer limit, Integer jobId, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		BootTablePage list = jobRecordService.getList(offset, limit, kUser.getuId(), jobId);
		return JsonUtils.objectToJson(list);
	}
	
	@RequestMapping("getLogContent.shtml")
	public String getLogContent(Integer recordId){
		try {
			String logContent = jobRecordService.getLogContent(recordId);
			return Result.success(logContent.replace("\r\n", "<br/>").replace("\n", "<br/>"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
}