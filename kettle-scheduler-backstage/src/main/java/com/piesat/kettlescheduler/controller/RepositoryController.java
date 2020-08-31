package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.BootTablePage;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.kettle.model.RepositoryTree;
import com.piesat.kettlescheduler.kettle.model.KRepositoryDto;
import com.piesat.kettlescheduler.model.KRepository;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.DataBaseRepositoryService;
import com.piesat.kettlescheduler.utils.DataValidate;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repository/")
public class RepositoryController {

	@Autowired
	private DataBaseRepositoryService dataBaseRepositoryService;

	@RequestMapping("database/getJobTree.shtml")
	public String getJobTree(Integer repositoryId){
		try {
			List<RepositoryTree> repositoryTreeList = dataBaseRepositoryService.getTreeList(repositoryId);
			List<RepositoryTree> newRepositoryTreeList = new ArrayList<RepositoryTree>();
			for(RepositoryTree repositoryTree : repositoryTreeList){
				if ("0".equals(repositoryTree.getParent())){
					repositoryTree.setParent("#");
				}
				if (repositoryTree.getId().indexOf("@") > 0){
					repositoryTree.setIcon("none");
				}
				if (Constant.TYPE_TRANS.equals(repositoryTree.getType())){
					Map<String,String> stateMap = new HashMap<String, String>();
					stateMap.put("disabled", "false");
					repositoryTree.setState(stateMap);
				}
				newRepositoryTreeList.add(repositoryTree);
			}
			return JsonUtils.objectToJson(newRepositoryTreeList);
		} catch (KettleException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("database/getTransTree.shtml")
	public String getTransTree(Integer repositoryId){
		try {
			List<RepositoryTree> repositoryTreeList = dataBaseRepositoryService.getTreeList(repositoryId);
			List<RepositoryTree> newRepositoryTreeList = new ArrayList<RepositoryTree>();
			for(RepositoryTree repositoryTree : repositoryTreeList){
				if ("0".equals(repositoryTree.getParent())){
					repositoryTree.setParent("#");
				}
				if (repositoryTree.getId().indexOf("@") > 0){
					repositoryTree.setIcon("none");
				}
				if (Constant.TYPE_JOB.equals(repositoryTree.getType())){
					Map<String,String> stateMap = new HashMap<String, String>();
					stateMap.put("disabled", "false");
					repositoryTree.setState(stateMap);
				}
				newRepositoryTreeList.add(repositoryTree);
			}
			return JsonUtils.objectToJson(newRepositoryTreeList);
		} catch (KettleException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 测试数据库连接
	 *
	 * @param kRepositoryDto
	 * @return java.lang.String
	 **/
	@RequestMapping("database/ckeck.shtml")
	public String ckeck(KRepositoryDto kRepositoryDto){
		if (DataValidate.AllNotEmpty(kRepositoryDto)){
			try {
				KRepository kRepository = new KRepository();
				BeanUtils.copyProperties(kRepositoryDto, kRepository);
				if (dataBaseRepositoryService.ckeck(kRepository)){
					return Result.success("success");
				}else {
					return Result.success("fail");
				}
			} catch (KettleException e) {
				e.printStackTrace();
				return Result.success("fail");
			}
		}else {
			return Result.success("fail");
		}
	}

	@RequestMapping("database/getSimpleList.shtml")
	public String getSimpleList(HttpServletRequest request){
		try {
			KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
			return JsonUtils.objectToJson(dataBaseRepositoryService.getList(kUser.getuId()));
		} catch (KettleException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 资源库列表
	 *
	 * @param offset
	 * @param limit
	 * @param request
	 * @return java.lang.String
	 **/
	@RequestMapping("database/getList.shtml")
	public String getList(Integer offset, Integer limit, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		BootTablePage list = null;
		list = dataBaseRepositoryService.getList(offset, limit, kUser.getuId());
		return JsonUtils.objectToJson(list);
	}

	/**
	 * 资源库类型
	 *
	 * @param
	 * @return java.lang.String
	 **/
	@RequestMapping("database/getType.shtml")
	public String getType(){
		return JsonUtils.objectToJson(dataBaseRepositoryService.getRepositoryTypeList());
	}

	/**
	 * 资源库数据库访问模式
	 *
	 * @param
	 * @return java.lang.String
	 **/
	@RequestMapping("database/getAccess.shtml")
	public String getAccess(){
		return JsonUtils.objectToJson(dataBaseRepositoryService.getAccess());
	}

	@RequestMapping("database/getKRepository.shtml")
	public String getKRepository(Integer repositoryId){
		return Result.success(dataBaseRepositoryService.getKRepository(repositoryId));
	}

	@RequestMapping("database/insert.shtml")
	public String insert(KRepository kRepository, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		dataBaseRepositoryService.insert(kRepository, kUser.getuId());
		return Result.success();
	}

	@RequestMapping("database/update.shtml")
	public String update(KRepository kRepository, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		dataBaseRepositoryService.update(kRepository, kUser.getuId());
		return Result.success();
	}
	@RequestMapping("database/delete.shtml")
	public String delete(Integer repositoryId){
		dataBaseRepositoryService.delete(repositoryId);
		return Result.success();
	}
}
