package com.thinkgem.jeesite.modules.index.dao;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.SearchKeyInfo;



/**
 * 描述：
 *
 * @author yb
 * @version 1.0
 */
@Repository
public class SearchKeyInfoDao extends BaseDao<SearchKeyInfo>{
	public Page<SearchKeyInfo> getSearckKeyInfoList(int no, int size,String orderBy,String searchKeyWord){
		Page<SearchKeyInfo> plist=new Page<SearchKeyInfo>(no,size);
		String sql = "from SearchKeyInfo where invalid = 0";
		if(searchKeyWord!=null&&!"".equals(searchKeyWord)){
			sql += " and searchkey like :p1";
		}
		if (orderBy != null && !"".equals(orderBy)) {
			sql += " order by " + orderBy;
		}
		if(searchKeyWord!=null&&!"".equals(searchKeyWord)){
			searchKeyWord = "%" + searchKeyWord + "%";
			plist = this.find(plist,sql,new Parameter(searchKeyWord));
		}else{
			plist = this.find(plist,sql);
		}
		return plist;	
	}
}
