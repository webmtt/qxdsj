package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.SearchCondItems;

@Repository
public class SearchCondItemsDao extends BaseDao<SearchCondItems>{
	public List<SearchCondItems> getSearchCondList(String itemtype) {
		String sql="from SearchCondItems where itemtype = :p1 order by orderno";
		return this.find(sql, new Parameter(itemtype));
	}
	public Integer getMaxId() {
		String sql="select max(id) from BMD_SEARCHCONDITEMS";
		List list=this.findBySql(sql);
		Integer maxId=Integer.valueOf(list.get(0).toString());
		return maxId;
	}
	public void deleteCondItems(Integer id,Integer invalid) {
		String sql="";
		if(invalid==0){ //设为无效
			sql="update SearchCondItems set invalid=1 where id=:p1";
		}else{//设为有效
			sql="update SearchCondItems set invalid=0 where id=:p1";
		}
		this.update(sql,new Parameter(id));
	}
}
