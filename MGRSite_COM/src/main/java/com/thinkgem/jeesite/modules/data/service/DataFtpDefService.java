package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.CategoryDataReltDao;
import com.thinkgem.jeesite.modules.data.dao.DataFtpDefDao;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataFtpDef;


@Service
public class DataFtpDefService extends BaseService{
	@Autowired
	private DataFtpDefDao dataFtpDefDao;
	
	public List<DataFtpDef> findDataFtpDef(){
		return this.dataFtpDefDao.findRow();
	}
	public Page<Object[]> findPageByName(Page<Object[]> page,String categoryid,String dataCode){
		return this.dataFtpDefDao.findPageByName(page,  categoryid, dataCode);
	}
	public Page<DataFtpDef> getDataDefPage(String dataCode, Page<DataFtpDef> page) {
		return dataFtpDefDao.getDataDefPage(dataCode,page);
	}

	public List<DataFtpDef> findDataFtpDefByCode(String dataCode) {
		return dataFtpDefDao.findDataFtpDefByCode(dataCode);
	}
	@Transactional
	public void updatedDataDef(DataFtpDef df) {
		dataFtpDefDao.save(df);		
	}
	@Transactional
	public void delDataFtpDef(String datacode) {
		dataFtpDefDao.delDataFtpDef(datacode);
		
	}

	
}
