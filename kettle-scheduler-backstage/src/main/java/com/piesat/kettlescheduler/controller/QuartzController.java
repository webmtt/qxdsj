package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.BootTablePage;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.model.KQuartz;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.QuartzService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/quartz/")
public class QuartzController {

	@Autowired
	private QuartzService quartzService;
	
	@RequestMapping("getSimpleList.shtml")
	public String simpleList(HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		return JsonUtils.objectToJson(quartzService.getList(kUser.getuId()));
	}
	@RequestMapping("getQuartz.shtml")
	public String getQuartz(Integer quartzId){
		return Result.success(quartzService.getQuartz(quartzId));
	}

	@RequestMapping("getList.shtml")
	public String getList(Integer offset, Integer limit, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		BootTablePage list = quartzService.getList(offset, limit, kUser.getuId());
		return JsonUtils.objectToJson(list);
	}
	@RequestMapping("insert.shtml")
	public String insert(KQuartz kQuartz, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		quartzService.insert(kQuartz, kUser.getuId());
		return Result.success();
	}
	@RequestMapping("delete.shtml")
	public String delete(Integer quartzId){
		quartzService.delete(quartzId);
		return Result.success();
	}
	@RequestMapping("update.shtml")
	public String update(KQuartz kQuartz, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		try{
			quartzService.update(kQuartz, kUser.getuId());
			return Result.success();
		}catch(Exception e){
			return Result.success(e.toString());
		}
	}
}
