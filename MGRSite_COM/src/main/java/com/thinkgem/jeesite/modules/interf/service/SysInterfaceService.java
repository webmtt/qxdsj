/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interf.service;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.interf.dao.SysInterfaceDao;
import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.mybatis.common.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 树结构生成Service
 * @author zhaoxiaojun
 * @version 2019-12-05
 */
@Service
//@Transactional(readOnly = true)
public class SysInterfaceService extends TreeService<SysInterfaceDao, SysInterface> {
	@Autowired
	private SysInterfaceDao sysInterfaceDao;

	public SysInterface get(String id) {
		return super.get(id);
	}
	
	public List<SysInterface> findList(SysInterface sysInterface) {
		if (StringUtils.isNotBlank(sysInterface.getParentIds())){
			sysInterface.setParentIds(","+sysInterface.getParentIds()+",");
		}
		return super.findList(sysInterface);
	}
	
	@Transactional(readOnly = false)
	public void save(SysInterface sysInterface) {
		super.save(sysInterface);
	}

	@Transactional(readOnly = false)
	public void deleteById(String  id) {
		sysInterfaceDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public List<SysInterface> findAllList(){
		return sysInterfaceDao.findAllList();
	}

	@Transactional(readOnly = false)
	public void insertInter(SysInterface sysInterface) {
		sysInterfaceDao.insertInter(sysInterface);
	}
}