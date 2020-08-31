package com.thinkgem.jeesite.modules.index.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.HostSubject;
import com.thinkgem.jeesite.modules.index.entity.PortalPicShowDef;

@Repository
public class PortalPicShowDao extends BaseDao<PortalPicShowDef> {
	public List<PortalPicShowDef> getAll(String area) {
		return find("from PortalPicShowDef where invalid=0 and areaItem like :p1 order by orderNo",new Parameter("%"+","+area+","+"%"));
	}
	public PortalPicShowDef  Delpps(String picCode ){
		List<PortalPicShowDef> pList=new ArrayList<PortalPicShowDef>();
		String sql="from PortalPicShowDef where picCode=:p1";
		pList=this.find(sql, new Parameter(picCode));
		return pList.get(0);
		
	}
	public PortalPicShowDef findByID(String picCode) {
		return this.get(picCode);
	}
	public void delpps(String picCode){
		this.update("delete from PortalPicShowDef where picCode = "+picCode+"");
	}
	public void delById(String picCode){
		update("delete from PortalPicShowDef where picCode=:p1",new Parameter(picCode));
	}
	
	public void updateTel(String chnName, int invalid, Integer orderNo,String picCode) {
		String sql="update PortalPicShowDef set chnName=:p1,invalid=:p2,orderNo=:p3 where picCode=:p4";
		this.update(sql, new Parameter(chnName,invalid,orderNo,picCode));		
	}
	
}
