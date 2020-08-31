package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.SearchSetDef;

@Repository
public class SearchSetDefDao extends BaseDao<SearchSetDef>{

	public  Page<SearchSetDef> getByPage(Page<SearchSetDef> page, String searchsetcode) {
		String sql="from SearchSetDef where searchsetcode like :p1";
		return this.find(page,sql,new Parameter("%"+searchsetcode+"%"));
	}

	public Page<SearchSetDef> getByPage(Page<SearchSetDef> page) {
		String sql="from SearchSetDef ";
		return this.find(page,sql,new Parameter());
	}

	public Integer getMaxId() {
		String sql="select max(id) from BMD_SEARCHSETDEF ";
		List list=this.findBySql(sql);
		Integer id=Integer.valueOf(list.get(0).toString());
		return id;
	}

	public SearchSetDef getsearchSetDefById(Integer id) {
		String sql="from SearchSetDef  where id =:p1 ";
		List<SearchSetDef> list=this.find(sql, new Parameter(id));
		return list.get(0);
	}
	public List<SearchSetDef> getsearchSetDefListById(String  searchsetcode) {
		String sql="from SearchSetDef  where searchsetcode =:p1 order by orderno";
		return this.find(sql, new Parameter(searchsetcode));
	}
	public List<SearchSetDef> getsearchSetDefListBy(String  searchsetcode,String searchgroupcode) {
		String sql="from SearchSetDef  where  searchsetcode =:p1 and searchgroupcode=:p2 ";
		return this.find(sql, new Parameter(searchsetcode,searchgroupcode));
	}

	public void delSearchSetDefById(Integer id) {
		String sql="update  SearchSetDef  set invalid=1 where id =:p1 ";
		this.update(sql, new Parameter(id));
	}
	
}
