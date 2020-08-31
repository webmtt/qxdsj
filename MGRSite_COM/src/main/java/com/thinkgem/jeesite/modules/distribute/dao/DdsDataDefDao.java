package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsDataDef;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class DdsDataDefDao extends BaseDao<DdsDataDef> {

	public List<DdsDataDef> findDdsDataDefList() {
		String hql = "from DdsDataDef where invalid=0 order by dataId";
		return this.find(hql);
		
	}
	public List<DdsDataDef> findDdsDataDefList(String dataId) {
		String hql = "from DdsDataDef where dataId=:p1 and  invalid=0";
		return this.find(hql,new Parameter(dataId));
		
	}
	public Page<DdsDataDef> getDdsDataDefPage(Page<DdsDataDef> page, DdsDataDef ddsDataDef, String dataId, String hostName) {
		String hql = "from DdsDataDef ";
		page.setOrderBy("id desc");
		if(dataId!=null && !"".equals(dataId)){
			if(hostName=="all" || "all".equals(hostName)){
				hql+=" where  dataId =:p1 ";
				//hql+=" order by dataId";
				return this.find(page,hql,new Parameter(dataId));
			}else if(hostName==null || "".equals(hostName)){
				hql+=" where  dataId = :p1 ";
				//hql+=" order by dataId";
				return this.find(page,hql,new Parameter(dataId));
			}else{
				hql+=" where  dataId = :p1 and hostName like :p2";
				//hql+=" order by dataId";
				return this.find(page,hql,new Parameter(dataId,"%"+hostName+"%"));
			}
		}else if(dataId==null || "".equals(dataId)){
			if(hostName=="all" || "all".equals(hostName)){
				//hql+=" order by dataId";
				return this.find(page,hql);	
			}else if(hostName==null || "".equals(hostName)){
				hql+=" order by dataId";
				return this.find(page,hql);		
			}else{
				hql+=" where  hostName like :p1";
				//hql+=" order by dataId";
				return this.find(page,hql,new Parameter("%"+hostName+"%"));
			}
		}else{
			//hql+=" order by dataId";
			return this.find(page,hql);
		}
		
		
	}
	public void delete(int Id) {
		String sql="from DdsDataDef  where id=:p1";
		DdsDataDef d = this.getByHql(sql, new Parameter(Id));
		String hql="";
		if(d.getInvalid()==0){
			hql="update DdsDataDef set invalid=1 where id=:p1";
		}
		if(d.getInvalid()==1){
			hql="update DdsDataDef set invalid=0 where id=:p1";
		}
		this.update(hql,new Parameter(Id));
		
	}
	
	public int findMaxId(){
		String hql = "from DdsDataDef where id=(select max(id) from DdsDataDef)";
		DdsDataDef d = this.getByHql(hql);
		return d.getId();	
	}
	
	public List<String> findHostName(){
		//String hql = "from DdsDataDef where hostName in (select distinct hostName from DdsDataDef)";
		String hql = "select distinct hostName from DdsDataDef";
		List<String> dList = this.find(hql);
		List<String> list = new ArrayList<String>();
		list.add("all");
		for(int i=0;i<dList.size();i++){
			if(dList.get(i)!=null && !"".equals(dList.get(i))){
				list.add(dList.get(i));
			}
		}
		return list;	
	}
	
	public int updateDdsDataDef(DdsDataDef ddsDataDef){
		String hql = "update DdsDataDef set dataFormat=:p1, nameFormat=:p2,timeFormat=:p3, "
				+ "pushTargetPath=:p4,searchTargetPath=:p5,hostName=:p6,"
				+ "timeSpan=:p7,spanUnit=:p8,delay=:p9,delayUnit=:p10,"
				+ "delayMinute=:p11,timeRate=:p12,rateUnit=:p13 where id=:p14";
		 return this.update(hql,
				new Parameter(ddsDataDef.getDataFormat(), ddsDataDef.getNameFormat(),ddsDataDef.getTimeFormat(),
						ddsDataDef.getPushTargetPath(),ddsDataDef.getSearchTargetPath(),ddsDataDef.getHostName(),
						ddsDataDef.getTimeSpan(),ddsDataDef.getSpanUnit(),ddsDataDef.getDelay(),
				ddsDataDef.getDelayUnit(),ddsDataDef.getDelayMinute(),ddsDataDef.getTimeRate(),
				ddsDataDef.getRateUnit(),ddsDataDef.getId()));	
		
	}
}
