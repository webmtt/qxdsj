package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;







import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.entity.DataDef;

@Repository
public class CategoryDataReltDao extends BaseDao<CategoryDataRelt>{
	public List<CategoryDataRelt> findAll(){
		return this.find("from CategoryDataRelt order by orderno");
	}
	public List<CategoryDataRelt> findCategoryDataReltById(int id){
		return this.find("from CategoryDataRelt where categoryid =:p1 order by orderno",new Parameter(id));
	}
	public List<CategoryDataRelt> findCategoryDataReltByDataCode(String dataCode){
		List<CategoryDataRelt> list = this.find("from CategoryDataRelt where datacode =:p1 order by orderno",new Parameter(dataCode));
		return list;
	}
	public List<CategoryDataRelt> findCategoryDataReltByDataCodeid(String dataCode,Integer categoryid){
		List<CategoryDataRelt> list = this.find("from CategoryDataRelt where datacode =:p1 and categoryid=:p2 order by orderno",new Parameter(dataCode,categoryid));
		return list;
	}
	public void delCategoryDataRelt(String datacode,Integer categoryid){
		String sql="delete from CategoryDataRelt where datacode=:p1 and categoryid=:p2";
		this.update(sql,new Parameter(datacode,categoryid));
	}
	public List<CategoryDataRelt> findCategoryListBycateId(Integer id) {
		String sql="from CategoryDataRelt where categoryid=:p1";
		return this.find(sql, new Parameter(id));
	}
	public Page<CategoryDataRelt> fondCateGoryByPage(String categoryid, Page<CategoryDataRelt> page) {
		String sql="from CategoryDataRelt where categoryid=:p1";
		return this.find(page, sql, new Parameter(Integer.parseInt(categoryid)));
	}
	@Override
	public void save(CategoryDataRelt entity) {
		super.save(entity);
	}
	public int getId() {
		String sql="select max(id) from BMD_CATEGORYDATARELT";
		List<Object> list=this.findBySql(sql);
		int id=0;
		for(Object o:list){
			if(o!=null){
				id=Integer.parseInt(o.toString());				
			}else{
				id=0;
			}
		}
		return id;
	}
	public CategoryDataRelt findCategoryDataRelt(String dataCode, String pid) {
		String sql="from CategoryDataRelt where categoryid=:p1 and datacode=:p2";
		List<CategoryDataRelt> list=this.find(sql, new Parameter(Integer.parseInt(pid),dataCode));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public List<CategoryDataRelt> findCategoryList(String categoryid) {
		String sql="from CategoryDataRelt where categoryid=:p1 order by orderno";
		return this.find(sql, new Parameter(Integer.parseInt(categoryid)));
	}
	public List<Object[]> findCategoryListById(String categoryid) {
		String sql="Select datacode,chnname From bmd_datadef Where  datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1)";
		return this.findBySql(sql, new Parameter(Integer.parseInt(categoryid)));
	}
	//Ftp下载的资料
	public List<Object[]> findFtpListById(String categoryid) {
		String sql="Select datacode,chnname From bmd_datadef Where  datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) and (ServiceMode=4 or ServiceMode=5 or ServiceMode=12 or ServiceMode=13)";
		return this.findBySql(sql, new Parameter(Integer.parseInt(categoryid)));
	}
	public List<Object[]> findCategoryListCById(String categoryid) {
		String sql="Select datacode,chnname From bmd_datadef Where datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) and (ServiceMode=1 or ServiceMode=9 or ServiceMode=13)";
		return this.findBySql(sql, new Parameter(Integer.parseInt(categoryid)));
	}
	public List<Object[]> findCategoryListSort(String categoryid) {
		String sql="Select a.id,b.chnname,a.orderno  From BMD_CATEGORYDATARELT a left Join (Select * From bmd_datadef) b On a.datacode=b.datacode Where a.categoryid=:p1 Order By a.orderno";
		return this.findBySql(sql, new Parameter(Integer.parseInt(categoryid)));
	}
}
