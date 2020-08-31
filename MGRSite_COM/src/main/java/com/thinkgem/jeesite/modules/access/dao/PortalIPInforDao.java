package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.PortalIPInfor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortalIPInforDao extends BaseDao<PortalIPInfor> {
	public List<PortalIPInfor> findAll(){
		return this.find("from PortalIPInfor");
	}
	public List<PortalIPInfor> findByProName(String proName){
		return this.find("from PortalIPInfor where proname=:p1",new Parameter(proName));
	}
	public List<Object> findProvinces(String type){
		Session session = this.getSession();
		Query query=session.createSQLQuery("select proname from PORTAL_IPINFOR  where sourcetype='"+type+"' group by proname");
		List<Object> list = query.list();
		return list;
	}
}
