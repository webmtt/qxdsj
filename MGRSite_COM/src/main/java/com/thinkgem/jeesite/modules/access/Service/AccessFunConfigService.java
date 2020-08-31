package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.access.dao.AccessFunConfigDao;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccessFunConfigService extends BaseService {
	@Autowired
	private AccessFunConfigDao accessFunConfigDao;
	@Transactional
	public List<AccessFunConfig> findFirstItems() {
		return this.accessFunConfigDao.findFirstItems();
	}

	public List<AccessFunConfig> findSubItems(String menuId) {
		// TODO Auto-generated method stub
		return this.accessFunConfigDao.findSubItems(menuId);
	}
	public List<Object[]> findSubItems1(String menuId) {
		return this.accessFunConfigDao.findSubItems1(menuId);
	}
	public AccessFunConfig findAccessFunConfigByID(String id){
		
		return this.accessFunConfigDao.findAccessFunConfigByID(id);
	}

	public List<AccessFunConfig> findAll() {
		
		return accessFunConfigDao.findAllByPid();
	}

	public List<AccessFunConfig> findItems(String menuId) {
		return this.accessFunConfigDao.findItems(menuId);
	}
}
