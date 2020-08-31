/*
 * @(#)GroundDataService.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.configure.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.configure.dao.FunItmesDao;
import com.thinkgem.jeesite.modules.configure.entity.FunItmes;


/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Component
@Transactional(readOnly = true)
@Service
public class FunItmesService extends BaseService{
	@Autowired
	private FunItmesDao funitmesdao;
	public FunItmes getFunItmes(Integer id){
		return funitmesdao.get(id);
	}
	@Transactional(readOnly = false)
	public void saveFunItems(FunItmes funItmes){
		funitmesdao.clear();
		this.funitmesdao.save(funItmes);
	}
	@Transactional(readOnly = false)
	public void deleteFunItems(List<FunItmes> list){
		for(int i=0;i<list.size();i++){
			this.funitmesdao.save(list.get(i));
		}
		
	}
	public List<FunItmes> getFunItemsListParentIDZero(){
		return funitmesdao.getFunItemsListParentIDZero();
	}
	
	public FunItmes getFunItmesById(String id){
		return this.funitmesdao.get(id);
	}
	public List<FunItmes> findAllList(String areaItem){
		List<FunItmes> list=funitmesdao.findAllList(areaItem);
		return list;
		
	}
}
