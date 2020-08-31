/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.HostSubject;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class HostSubjectDao extends BaseDao<HostSubject>{
	public List<HostSubject> getHostSubjectList(String area){
		return this.find("from HostSubject where invalid = 0 and areaItem like :p1 order by orderno",new Parameter("%"+","+area+","+"%"));
	}
	public  HostSubject DelHost(Integer id){
		List<HostSubject> pList=new ArrayList<HostSubject>();
		String sql="from HostSubject where id=:p1";
		pList=this.find(sql, new Parameter(id));
		return pList.get(0);
		
	}
	public HostSubject findByID(Integer ID) {
		List<HostSubject> pList=new ArrayList<HostSubject>();
		String sql="from HostSubject where id=:p1";
		pList=this.find(sql, new Parameter(ID));
		return pList.get(0);
		
	}
	public void delColumn(String ID){
		this.update("delete from HostSubject where id = "+ID+"");
	}
	public void delById(int ID){
		update("delete from HostSubject where id=:p1",new Parameter(ID));
	}
	public void updateTel(int orderno,  int i,Integer ID) {
		String sql="update HostSubject set orderno=:p1,invalid=:p2 where id=:p3";
		this.update(sql, new Parameter(orderno,i,ID));		
	}
	
}
