package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.DataSearchDefDao2;
import com.thinkgem.jeesite.modules.data.entity.DataSearchDef2;



@Service
public class DataSearchDefService2 extends BaseService{
	@Autowired
	private DataSearchDefDao2 dataSearchDefDao;
	public Page<DataSearchDef2> getByPage(Page<DataSearchDef2> page, String datacode) {
		return dataSearchDefDao.getByPage(page,datacode);
	}
	public List<DataSearchDef2> getListByDataCode(String datacode) {
		return dataSearchDefDao.getListByDataCode(datacode);
	}
	public List<DataSearchDef2> getListByDatapCode(String datacode) {
		return dataSearchDefDao.getListByDatapCode(datacode);
	}
	public List<DataSearchDef2> getListByCodeFlag(String code,String flag) {
		return dataSearchDefDao.getListByCodeFlag(code,flag);
	}
	public Page<DataSearchDef2> getByPage(Page<DataSearchDef2> page) {
		return dataSearchDefDao.getByPage(page);
	}
	@Transactional
	public void saveDataSearchDef(DataSearchDef2 dataSearchDef) {
		this.dataSearchDefDao.save(dataSearchDef);
		
	}
	public DataSearchDef2 getDataSearchDefByDataCode(String datacode) {
		return dataSearchDefDao.getDataSearchDefByDataCode(datacode);
	}
	@Transactional
	public void delDataSearchDefByDataCode(String datacode) {
		this.dataSearchDefDao.delDataSearchDefByDataCode(datacode);
	}
	@Transactional
	public void updateDataSearchDefByDataCode(String datacode,String interfacesetcode) {
		this.dataSearchDefDao.updateDataSearchDefByDataCode(datacode, interfacesetcode);
	}
	
}
