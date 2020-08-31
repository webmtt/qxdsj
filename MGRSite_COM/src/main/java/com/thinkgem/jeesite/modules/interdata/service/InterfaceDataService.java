/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interdata.service;

import com.thinkgem.jeesite.modules.interdata.entity.InterDeploy;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceDefine;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.modules.interdata.dao.InterfaceDataDao;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceData;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API接口展示Service
 * @author zhaoxiaojun
 * @version 2019-12-06
 */
@Service
@Transactional(readOnly = true)
public class InterfaceDataService extends CrudService<InterfaceDataDao, InterfaceData> {

	@Autowired
	private InterfaceDataDao interfaceDataDao;

	public InterfaceData get(String id) {
		return super.get(id);
	}

	public List<InterfaceData> findList(InterfaceData interfaceData) {
		return super.findList(interfaceData);
	}

	public Page<InterfaceData> findPage(Page<InterfaceData> page, InterfaceData interfaceData) {
		return super.findPage(page, interfaceData);
	}
	
	@Transactional(readOnly = false)
	public void save(InterfaceData interfaceData) {
		super.save(interfaceData);
	}
	
	@Transactional(readOnly = false)
	public void delete(InterfaceData interfaceData) {
		super.delete(interfaceData);
	}

	public List<InterfaceData> getAllInter() {
		return interfaceDataDao.getAllInter();
	}

    public List<InterfaceDefine> getInterface(String id) {
		return interfaceDataDao.getInterface(id);
    }

    public List<Map<String, Object>> projectcdinter(String id) {
        return interfaceDataDao.projectcdinter(id);
    }

    public List<Map<String, Object>> projectcdinterall() {
        return interfaceDataDao.projectcdinterall();
    }

    public String projectcdinterName(String custom_api_id) {
        return interfaceDataDao.projectcdinterName(custom_api_id);
    }

    public List<Map<String, Object>> interElement(String id) {
        return interfaceDataDao.interElement(id);
    }

    public Map<String, Object> interElementName(String param_id) {
        return interfaceDataDao.interElementName(param_id);
    }

    public List<Map<String, Object>> getFcstLevel() {
		return interfaceDataDao.getFcstLevel();
    }

	public List<Map<String, Object>> getNetCodes() {
		return interfaceDataDao.getNetCodes();
	}

	public List<Map<String, Object>> getSoilDepths() {
		return interfaceDataDao.getSoilDepths();
	}

	public List<Map<String, Object>> getAdminCodes() {
		return interfaceDataDao.getAdminCodes();
	}

	public List<Map<String, Object>> getFcstEle(String id) {
		return interfaceDataDao.getFcstEle(id);
	}

	public List<Map<String, Object>> getElements(String id) {
		return interfaceDataDao.getElements(id);
	}

	public List<Map<String, Object>> getInterElement(String[] inters) {
		return interfaceDataDao.getInterElement(inters);
	}

	@Transactional(readOnly = false)
	public int addInterValue(InterDeploy interDeploy) {
		return interfaceDataDao.addInterValue(interDeploy);

	}

	@Transactional(readOnly = false)
	public int updateInterValue(InterDeploy interDeploy) {

		return interfaceDataDao.updateInterValue(interDeploy);
	}

	public List<InterDeploy> getAllInterValue() {
		return interfaceDataDao.getAllInterValue();
	}
}