/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.arithmetic.service;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.dao.SupArithmeticsPackageDao;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.entity.SupArithmeticsPackage;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 算法信息管理Service
 * @author yang.kq
 * @version 2019-11-22
 */
@Service
@Transactional(readOnly = true)
public class SupArithmeticsPackageService extends CrudService<SupArithmeticsPackageDao, SupArithmeticsPackage> {
	@Autowired
	private SupArithmeticsPackageDao supArithmeticsPackageDao;

	public SupArithmeticsPackage findListByName(SupArithmeticsPackage supArithmeticsPackage) {
		return supArithmeticsPackageDao.findListByName(supArithmeticsPackage);
	}
	public SupArithmeticsPackage get(String id) {
		return super.get(id);
	}
	
	public List<SupArithmeticsPackage> findList(SupArithmeticsPackage supArithmeticsPackage) {
		return super.findList(supArithmeticsPackage);
	}
	
	public Page<SupArithmeticsPackage> findPage(Page<SupArithmeticsPackage> page, SupArithmeticsPackage supArithmeticsPackage) {
		return super.findPage(page, supArithmeticsPackage);
	}

	public Page<SupArithmeticsPackage> findPages(Page<SupArithmeticsPackage> page, SupArithmeticsPackage supArithmeticsPackage) {
		return super.findPages(page, supArithmeticsPackage);
	}

	@Transactional(readOnly = false)
	public void save(SupArithmeticsPackage supArithmeticsPackage) {
		super.save(supArithmeticsPackage);
	}
	
	@Transactional(readOnly = false)
	public void delete(SupArithmeticsPackage supArithmeticsPackage) {
		super.delete(supArithmeticsPackage);
	}

	@Transactional(readOnly = false)
	public int selectCounts(SupArithmeticsPackage supArithmeticsPackage) {
		return supArithmeticsPackageDao.selectCounts(supArithmeticsPackage);
	}
	@Transactional(readOnly = false)
	public int updateAriState(SupArithmeticsPackage supArithmeticsPackage) {
		return supArithmeticsPackageDao.updateAriState(supArithmeticsPackage);
	}
	public SupArithmeticsPackage findObjectByAirName(String ariName){
		return supArithmeticsPackageDao.findObjectByAirName(ariName);
	}

}