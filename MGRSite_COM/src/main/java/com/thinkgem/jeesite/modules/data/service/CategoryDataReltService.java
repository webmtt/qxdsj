package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.CategoryDataReltDao;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;

@Service
public class CategoryDataReltService extends BaseService{
	@Autowired
	private CategoryDataReltDao categoryDataReltDao;
	
	public List<CategoryDataRelt> findCategoryDataRelt(){
		return this.categoryDataReltDao.findAll();
	}
	
	public List<CategoryDataRelt> findCategoryDataReltById(int id){
		return this.categoryDataReltDao.findCategoryDataReltById(id);
	}
	public CategoryDataRelt findDataReltById(int id){
		return this.categoryDataReltDao.get(id);
	}
	
	public List findCategoryDataReltByDataCode(String dataCode){
		return this.categoryDataReltDao.findCategoryDataReltByDataCode(dataCode);
	}
	public List<CategoryDataRelt> findCategoryDataReltByDataCodeid(String dataCode,Integer categoryid){
		return this.categoryDataReltDao.findCategoryDataReltByDataCodeid(dataCode,categoryid);
	}
	@Transactional
	public void save(CategoryDataRelt categoryDataRelt){
//		categoryDataReltDao.save(categoryDataRelt);
		categoryDataReltDao.saveData(categoryDataRelt);
	}
	public void delCategoryDataRelt(String datacode,Integer categoryid){
		categoryDataReltDao.delCategoryDataRelt(datacode, categoryid);
	}

	public List<CategoryDataRelt> findCategoryListBycateId(Integer id) {
		return categoryDataReltDao.findCategoryListBycateId(id);
	}

	public Page<CategoryDataRelt> findCateGoryByPage(String categoryid, Page<CategoryDataRelt> page) {
		return categoryDataReltDao.fondCateGoryByPage(categoryid,page);
	}

	public int getId() {
		return categoryDataReltDao.getId();
	}

	public CategoryDataRelt findCategoryDataReltByDataCode(String dataCode, String pid) {
		return categoryDataReltDao.findCategoryDataRelt(dataCode,pid);
	}

	public List<CategoryDataRelt> findCategoryList(String categoryid) {
		return categoryDataReltDao.findCategoryList(categoryid);
	}
	public List<Object[]> findCategoryListById(String categoryid) {
		return categoryDataReltDao.findCategoryListById(categoryid);
	}
	public List<Object[]> findFtpListById(String categoryid) {
		return categoryDataReltDao.findFtpListById(categoryid);
	}
	public List<Object[]> findCategoryListCById(String categoryid) {
		return categoryDataReltDao.findCategoryListCById(categoryid);
	}
	public List<Object[]> findCategoryListSort(String categoryid) {
		return categoryDataReltDao.findCategoryListSort(categoryid);
	}
}
