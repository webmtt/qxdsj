package com.thinkgem.jeesite.modules.data.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.DataDefDao;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DataDefService extends BaseService{
	@Autowired
	private DataDefDao dataDefDao;
	@Autowired
	private ComparasDao comparasDao;
	
	public List<DataDef> findDataDef(){
		return this.dataDefDao.findAll();
	}
	public List findDataDefByCodes(List<CategoryDataRelt> list){
		return this.dataDefDao.findDataDefByCodes(list);
	}
	public List<DataDef> findDataDefByParentId(Integer id){
		List<DataDef> dataDef = this.dataDefDao.findDataDefByParentId(id);
		return dataDef;
	}
	public DataDef findDataDefByDataCode(String dataCode){
		DataDef dataDef = this.dataDefDao.findDataDefByDataCode(dataCode);
		return dataDef;
	}
	public Page<DataDef> getSearchResultByKey(String keyWord, String subcontent, Page<DataDef> page) {
		Page<DataDef> p=dataDefDao.getSearchResultByKey(keyWord,subcontent,page);
		return p;
	}
	public Page<DataDef> getSearchResultByKey(String keyWord, Page<DataDef> page) {
		Page<DataDef> p=dataDefDao.getSearchResultByKey2(keyWord,page);
		return p;
	}
	public int getTotalPageByKey(String keyWord, String subcontent) {
		List<DataDef> list=dataDefDao.getTotalPageByKey(keyWord,subcontent);
		return list.size();
	}
	public List<String> getSearchResultByKey(String keyWord) {
		return dataDefDao.getSearchResultByKey(keyWord);
	}
	@Transactional
	public void save(DataDef dataDef){
//		dataDefDao.save(dataDef);
		dataDefDao.saveData(dataDef);
	}
	@Transactional
	public void delDataDef(String id){
		dataDefDao.delDataDef(id);
	}
	public DataDef getDataDefByCode(String datacode) {
		return dataDefDao.findDataDefByDataCode(datacode);
	}
	public Page findDataDefByCodes(String categoryid, String dataDefName, Page<DataDef> page) {
		return dataDefDao.fondDataDefByCodes(categoryid,dataDefName,page);
	}
	public List<Object[]> fondDataDefListByCodes(String categoryid, String dataDefName) {
		return dataDefDao.fondDataDefListByCodes(categoryid, dataDefName);
	}

	/**
	 * 批量导入
	 */
	@Transactional(readOnly = false)
	public void upload(InputStream in, MultipartFile file) {
		try {
			List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, file.getOriginalFilename());
			dataDefDao.upload(listob);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
