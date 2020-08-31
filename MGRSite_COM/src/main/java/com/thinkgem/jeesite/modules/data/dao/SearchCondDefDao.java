package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;

@Repository
public class SearchCondDefDao extends BaseDao<SearchCondDef>{

	public Page<SearchCondDef> getByPage(Page<SearchCondDef> page, String searchgroupcode) {
		String sql="from SearchCondDef where searchgroupcode like :p1 and invalid=0";
		return this.find(page, sql, new Parameter(searchgroupcode));
	}

	public Page<SearchCondDef> getByPage(Page<SearchCondDef> page) {
		String sql="from SearchCondDef ";
		return this.find(page, sql, new Parameter());
	}

	public Integer getMaxId() {
		String sql="select max(id) from BMD_SEARCHCONDDEF";
		List list=this.findBySql(sql);
		Integer maxId=Integer.valueOf(list.get(0).toString());
		return maxId;
	}
	public List<SearchCondDef> getSearchClistByCode(String searchsetcode) {
		String sql="from SearchCondDef where searchgroupcode in(select searchgroupcode from SearchSetDef where searchsetcode=:p1 )";
		return this.find(sql, new Parameter(searchsetcode));
	}
	public List<SearchCondDef> getSearchListByGroupCode(String GroupCode) {
		String sql="from SearchCondDef where searchgroupcode=:p1 order by orderno";
		return this.find(sql, new Parameter(GroupCode));
	}
	public List<SearchCondDef> getSearchListGroupCode(String GroupCode) {
		String sql="from SearchCondDef where searchgroupcode like :p1  order by orderno";
		return this.find(sql, new Parameter(GroupCode+"%"));
	}
	public SearchCondDef getSearchCondDefById(Integer id) {
		String sql="from SearchCondDef where id =:p1";
		List<SearchCondDef> list=this.find(sql, new Parameter(id));
		return list.get(0);
	}

	public void delSearchCondDefById(Integer id) {
		String sql="update SearchCondDef set invalid=1 where id=:p1";
		this.update(sql, new Parameter(id));
	}
	public void updateSearchCondDefById(String  searchgroupcode) {
		String sql="update SearchCondDef set groupdefaultsearch=0 where searchgroupcode=:p1";
		this.update(sql, new Parameter(searchgroupcode));
	}
	
}
