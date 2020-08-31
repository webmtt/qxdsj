package com.thinkgem.jeesite.modules.statistics.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;

@Repository
public class OrgInfoDao2 extends BaseDao<OrgInfo> {
	
	List<String> names=new ArrayList<String>();
	
	public List<OrgInfo> findAll() {
		return find("from OrgInfo");// 表中invalid为null
	}
	
	public List<OrgInfo> findById(String id){
		return this.find("from OrgInfo where id=:p1",new Parameter(id));
	}
	
	public String getOrgInfoName(String userRankId,String id){
		List<OrgInfo> list=findById(id);
		if(list.size()>0){
			names.add(list.get(0).getName());
			getOrgInfoName(userRankId,list.get(0).getParentId());
		}else{
			return listToString(userRankId,names);
		}
	
		return listToString(userRankId,names);
	}
	
	private String listToString(String userRankId,List<String> names){
		String name="";
		int size=names.size();
		if("1".equals(userRankId)){
			size-=1;
		}
		for(int i=size-1;i>=0;i--){
			name+="-"+names.get(i);
		}
		if(!"".equals(name)){
			name=name.substring(1);
		}
		return name;
	}

	/**
	 * 机构第一级
	 * @param flag flag为2时为国家级用户，flag为1时为非国家级用户
	 * @param parentid
	 * @return
	 */
	public List<OrgInfo> findByParentId(String userRankId, String parentid) {
		List<OrgInfo> list = new ArrayList<OrgInfo>();
		if ("2".equals(userRankId)) {
			list = this.find("from OrgInfo where parentid=:p1 and name not like '%气象局'", new Parameter(parentid));
		} else {
			list = this.find("from OrgInfo where parentid=:p1 and name like '%气象局' order by code", new Parameter(parentid));
		}
		return list;
	}
	
	public List<OrgInfo> findByParentId(String parentid) {
		return this.find("from OrgInfo where parentid=:p1", new Parameter(parentid));

	}
	
	public void emptyName(){
		names.clear();
	}

	public String getOrgByCode(String orgID) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOrgByCodeString(String orgID, int userRankID, int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
