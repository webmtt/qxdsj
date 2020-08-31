package com.thinkgem.jeesite.modules.data.service;

import java.io.InputStream;
import java.util.List;

import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.DataReferDefDao;
import com.thinkgem.jeesite.modules.data.entity.DataReferDef;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataReferDefService extends BaseService{
	@Autowired
	private DataReferDefDao dataReferDefDao;
	
	public List<DataReferDef> findDataReferDef(){
		return this.dataReferDefDao.findAll();
	}
	public List<DataReferDef> findDataReferDefByDataCode(String dataCode){
		return this.dataReferDefDao.findDataReferDefByDataCode(dataCode);
	}
	public Page<Object[]> findPageByName(Page<Object[]> page,String chnname,String categoryid,String dataCode){
		return this.dataReferDefDao.findPageByName(page, chnname, categoryid, dataCode);
	}
	public Page<DataReferDef> findDataReferDefPage(Page<DataReferDef> page,String dataCode){
		return dataReferDefDao.findDataReferDefPage(page, dataCode);
	}

	@Transactional
	public void delDataReferByCode(String datacode) {
		dataReferDefDao.delDataReferByCode(datacode);
	}

	
	@Transactional
	public void saveDataReferDefService(DataReferDef drd) {
		dataReferDefDao.saveData(drd);		
	}
	
	@Transactional
	public DataReferDef findDataReferDefByID(Integer referid) {
		return dataReferDefDao.findById(referid);		
	}
	
	
	
	@Transactional
	public  void saveUpdate(
			Integer referid,String datacode,String refername,
			Integer orderno,Integer invalid) {
		dataReferDefDao.saveUpdate(referid, datacode, refername, orderno, invalid);
	}
	
	@Transactional
	public void delDataReferDefService(Integer referid) {
		dataReferDefDao.delById(referid);
	}

	/**
	 * 批量导入
	 */
	@Transactional(readOnly = false)
	public void upload(InputStream in, MultipartFile file){
		try {
			List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, file.getOriginalFilename());
			dataReferDefDao.upload(listob);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
