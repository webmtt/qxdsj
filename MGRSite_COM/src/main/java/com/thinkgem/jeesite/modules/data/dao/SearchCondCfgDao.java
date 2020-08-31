package com.thinkgem.jeesite.modules.data.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.SearchCondCfg;

@Repository
public class SearchCondCfgDao extends BaseDao<SearchCondCfg>{

	public Page<SearchCondCfg> getByPage(Page<SearchCondCfg> page, String searchconfigcode) {
		String sql="from SearchCondCfg where searchconfigcode like :p1";
		return this.find(page, sql, new Parameter("%"+searchconfigcode+"%"));
	}

	public Page<SearchCondCfg> getByPage(Page<SearchCondCfg> page) {
		String sql="from SearchCondCfg";
		return this.find(page, sql);
	}

	public SearchCondCfg getSearchCondCfgByScc(String searchconfigcode) {
		String sql="from SearchCondCfg where searchconfigcode =:p1";
		List<SearchCondCfg> list =this.find(sql, new Parameter(searchconfigcode));
		return list.get(0);
	}
	public List<SearchCondCfg> getSearchCondCfgListByScc(String searchconfigcode) {
		String sql="from SearchCondCfg where searchconfigcode =:p1";
		List<SearchCondCfg> list =this.find(sql, new Parameter(searchconfigcode));
		return list;
	}
	public List<SearchCondCfg>  getSearchCondCfgList() {
		String sql="Select searchconfigcode,chndescription From BMD_SEARCHCONDCFG Group By searchconfigcode,chndescription Order By searchconfigcode";
		List<Object[]> list=this.findBySql(sql);
		List<SearchCondCfg> listall=new ArrayList<SearchCondCfg>();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			SearchCondCfg searchCondCfg=new SearchCondCfg();
			searchCondCfg.setSearchconfigcode(obj[0].toString());
			searchCondCfg.setChndescription(obj[1].toString());
			listall.add(searchCondCfg);
		}
		
		return listall;
	}
	public void delSearchCondCfgByScc(String searchconfigcode) {
		String sql="delete from SearchCondCfg where searchconfigcode =:p1";
		this.update(sql, new Parameter(searchconfigcode));
	}
	
}
