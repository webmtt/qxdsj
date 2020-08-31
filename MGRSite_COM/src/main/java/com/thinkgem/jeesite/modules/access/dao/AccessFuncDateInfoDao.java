package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncDateInfo;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccessFuncDateInfoDao extends BaseDao<AccessFuncDateInfo> {
	public List<AccessFuncInfo> findByDatePVNum(String date, String showType) {
		if(showType!=null&&!"".equals(showType)){
			return this.find("from AccessFuncInfo where accessdate=:p1 and sourceType=:p2", new Parameter(date,showType));
		}else{
			return this.find("from AccessFuncInfo where accessdate=:p1", new Parameter(date));
		}
	}
	
	public List<AccessFuncInfo> findByDateIPNum(String date) {
		return this.find("from AccessFuncInfo where accessdate=:p1", new Parameter(date));
	}
	
	public List<AccessFuncInfo> findByDateStayTime(String date, String showType) {
		if(showType!=null&&!"".equals(showType)){
			return this.find("from AccessFuncInfo where accessdate=:p1 and sourceType=:p2", new Parameter(date,showType));
		}else{
			return this.find("from AccessFuncInfo where accessdate=:p1", new Parameter(date));
		}
		
	}
	
	public List<AccessFuncDateInfo> findByDatePVNum(String date1, String date2, String showType) {
		if(showType!=null&&!"".equals(showType)){
			return this.find("from AccessFuncDateInfo where sourceType=:p1 and accessdate between :p2 and :p3", new Parameter(showType,date1,date2));
		}else{
			return this.find("from AccessFuncDateInfo where accessdate between :p1 and :p2", new Parameter(date1, date2));
		}
	}
	
	public List<AccessFuncDateInfo> findByDateIPNum(String date1, String date2) {
		return this.find("from AccessFuncDateInfo where accessdate between :p1 and :p2", new Parameter(date1, date2));
	}
	
	public List<AccessFuncDateInfo> findByDateStayTime(String date1, String date2, String showType) {
		if(showType!=null&&!"".equals(showType)){
			return this.find("from AccessFuncDateInfo where sourceType=:p1 and accessdate between :p2 and :p3", new Parameter(showType,date1,date2));
		}else{
			return this.find("from AccessFuncDateInfo where accessdate between :p1 and :p2", new Parameter(date1, date2));
		}
	
	}
	
	
}
