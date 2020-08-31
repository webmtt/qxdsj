package com.thinkgem.jeesite.modules.data.service;

import java.io.InputStream;
import java.util.List;

import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.DataLinksDao;
import com.thinkgem.jeesite.modules.data.entity.DataLinks;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DataLinksService extends BaseService{
	@Autowired
	private DataLinksDao dataLinksDao;
	
	public List<DataLinks> findDataLinks(){
		return this.dataLinksDao.findAll();
	}
	public List<DataLinks> findDataLinksByDataCode(String dataCode){
		return this.dataLinksDao.findDataLinksByDataCode(dataCode);
	}
	public List<DataLinks> findDataLinksByDataCode2(String dataCode){
		return this.dataLinksDao.findDataLinksByDataCode2(dataCode);
	}
	public void delDataLinkByCode(String datacode) {
		dataLinksDao.delDataLinkByCode(datacode);
	}

	public Page<DataLinks> findDataLinksPage(Page<DataLinks> page,String dataCode){
		return dataLinksDao.findDataLinksPage(page, dataCode);
	}
	public Page<Object[]> findDataLinksPages(Page<Object[]> page,String chnname,String categoryid,String dataCode){
		return dataLinksDao.findDataLinksPages(page, chnname, categoryid, dataCode);
	}

	@Transactional
	public void saveDataLinksService(DataLinks dataLinks) {
		dataLinksDao.save(dataLinks);
		
	}
	public DataLinks finddataLinksByID(Integer linkid) {		
		return dataLinksDao.findById(linkid);
	}

	@Transactional
	public void saveUpdate(Integer linkid, String datacode, Integer linktype, String linkname, String linkurl,
			Integer orderno, Integer invalid) {
		dataLinksDao.saveUpdate(linkid, datacode,linktype,linkname, linkurl,orderno,invalid);
	}
	@Transactional
	public void deldataLinksService(Integer linkid) {
		dataLinksDao.delById(linkid);		
	}

	/**
	 * 批量导入
	 */
	@Transactional(readOnly = false)
	public void upload(InputStream in, MultipartFile file) {
		try {
			List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, file.getOriginalFilename());
			dataLinksDao.upload(listob);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
