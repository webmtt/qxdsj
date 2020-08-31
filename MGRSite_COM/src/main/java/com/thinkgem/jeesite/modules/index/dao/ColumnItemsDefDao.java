package com.thinkgem.jeesite.modules.index.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.HostSubject;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;

@Repository
public class ColumnItemsDefDao extends BaseDao<ColumnItemsDef>{
	public List<ColumnItemsDef> findAll(String area){
		return find("from ColumnItemsDef where invalid = 0 and areaItem like :p1 and columnType in(select t.value from Dict t where t.type='columnType') order by orderNo",new Parameter("%"+","+area+","+"%"));
	}
	public ColumnItemsDef findByColumnItemID(int ID) {
		return this.get(ID);
	}
	public void delColumn(String columnItemID){
		this.update("delete from ColumnItemsDef where columnItemID = "+columnItemID+"");
	}
	public Page<ColumnItemsDef> getByPage(Page<ColumnItemsDef> page,String area) {
		Page<ColumnItemsDef> p=new Page<ColumnItemsDef>();
		 
		StringBuffer sb = new StringBuffer("from ColumnItemsDef where invalid = 0 and areaItem like :p1 and columnType in(select t.value from Dict t where t.type='columnType') order by orderNo");
			//sb.append(" order by columnItemID desc");
			p=this.find(page, sb.toString(),new Parameter("%"+","+area+","+"%"));
			return p;
		}
		
	
	public List<ColumnItemsDef> getTeleList() {		
		return this.find("from ColumnItemsDef order by columnItemID desc");
	}
	public  ColumnItemsDef Delcolumn(int columnItemID){
		List<ColumnItemsDef> pList=new ArrayList<ColumnItemsDef>();
		String sql="from ColumnItemsDef where columnItemID=:p1";
		pList=this.find(sql, new Parameter(columnItemID));
		return pList.get(0);
		
	}
	public void delById(int columnItemID){
		update("delete from ColumnItemsDef where columnItemID=:p1",new Parameter(columnItemID));
	}
	public void updateTel(String chnName, String linkURL, Integer showType, int i,String columnType,Integer columnItemID) {
		String sql="update ColumnItemsDef set chnName=:p1,linkURL=:p2,showType=:p3,orderNo=:p4,columnType=:p5 where columnItemID=:p6";
		this.update(sql, new Parameter(chnName,linkURL,showType,i,columnType,columnItemID));		
	}
	
	
	
}
