/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.stream.service;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.modules.stream.dao.SupArithmeticsStreamDao;
import com.thinkgem.jeesite.mybatis.modules.stream.entity.SupArithmeticsStream;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 算法池基本信息Service
 * @author yck
 * @version 2019-12-03
 */
@Service
@Transactional(readOnly = true)
public class SupArithmeticsStreamService extends CrudService<SupArithmeticsStreamDao, SupArithmeticsStream> {
	@Autowired
	private SupArithmeticsStreamDao supArithmeticsStreamDao;
	public SupArithmeticsStream get(String id) {
		return super.get(id);
	}
	@Transactional(readOnly = false)
	public void updateAriState(SupArithmeticsStream supArithmeticsStream){
		supArithmeticsStreamDao.updateAriState(supArithmeticsStream);
	}
	@Transactional(readOnly = false)
	public void updateStreamStatus(SupArithmeticsStream supArithmeticsStream){
		supArithmeticsStreamDao.updateStreamStatus(supArithmeticsStream);
	};
	public List<SupArithmeticsStream> findList(SupArithmeticsStream supArithmeticsStream) {
		return super.findList(supArithmeticsStream);
	}
	
	public Page<SupArithmeticsStream> findPage(Page<SupArithmeticsStream> page, SupArithmeticsStream supArithmeticsStream) {
		return super.findPage(page, supArithmeticsStream);
	}
	public Page<SupArithmeticsStream> findPages(Page<SupArithmeticsStream> page, SupArithmeticsStream supArithmeticsStream) {
		return super.findPages(page, supArithmeticsStream);
	}
	@Transactional(readOnly = false)
	public void save(SupArithmeticsStream supArithmeticsStream) {
		super.save(supArithmeticsStream);
	}
	
	@Transactional(readOnly = false)
	public void delete(SupArithmeticsStream supArithmeticsStream) {
		super.delete(supArithmeticsStream);
	}

	public SupArithmeticsStream findObjectByAirName(String streamName){
		return supArithmeticsStreamDao.findObjectByAirName(streamName);
	}
}