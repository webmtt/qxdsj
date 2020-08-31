package com.piesat.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.piesat.core.dto.BootTablePage;
import com.piesat.core.dto.ResultDto;
import com.piesat.core.model.KQuartz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.common.toolkit.Constant;
import com.piesat.core.model.KUser;
import com.piesat.web.service.QuartzService;
import com.piesat.web.utils.JsonUtils;

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
		return ResultDto.success(quartzService.getQuartz(quartzId));
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
		return ResultDto.success();
	}
	@RequestMapping("delete.shtml")
	public String delete(Integer quartzId){
		quartzService.delete(quartzId);
		return ResultDto.success();
	}
	@RequestMapping("update.shtml")
	public String update(KQuartz kQuartz, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		try{
			quartzService.update(kQuartz, kUser.getuId());
			return ResultDto.success();
		}catch(Exception e){
			return ResultDto.success(e.toString());
		}
	}
}
