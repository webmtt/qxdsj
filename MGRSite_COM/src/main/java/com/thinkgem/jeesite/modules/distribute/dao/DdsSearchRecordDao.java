package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchRecord;
import org.springframework.stereotype.Repository;

@Repository
public class DdsSearchRecordDao extends BaseDao<DdsSearchRecord> {
		
	 public Page<DdsSearchRecord> getDdsSearchRecordPage(Page<DdsSearchRecord> page, DdsSearchRecord ddsSearchRecord,
                                                         String dataId, String timeCondBegin, String retriveStatus) {
		 String sql = "from DdsSearchRecord ";
		 //全不空
		 if(dataId != null & !"".equals(dataId) & timeCondBegin != null & !"".equals(timeCondBegin)
				& retriveStatus != null & !"".equals(retriveStatus) ){
			 sql += "where dataId =:p1 and timeCondBegin like:p2 and retriveStatus like:p3 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter(dataId,"%"+timeCondBegin+"%","%"+retriveStatus+"%"));
		 }
		 // d 不空  t 空  r 空
		 else if((dataId !=null & !"".equals(dataId)) & (timeCondBegin==null | "".equals(timeCondBegin))
				 & (retriveStatus == null | "".equals(retriveStatus) )){
			 sql += "where dataId =:p1 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter(dataId));
		 }
		// d 不空  t 不空  r 空
		 else if((dataId !=null & !"".equals(dataId)) & (timeCondBegin!=null & !"".equals(timeCondBegin))
				 & (retriveStatus == null | "".equals(retriveStatus) )){
			 sql += "where dataId =:p1 and timeCondBegin like:p2 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter(dataId,"%"+timeCondBegin+"%"));
		 }
		// d 不空  t 空  r 不空
		 else if((dataId !=null & !"".equals(dataId)) & (timeCondBegin==null | "".equals(timeCondBegin))
						 & (retriveStatus != null & !"".equals(retriveStatus) ) ){
			 sql += "where dataId =:p1 and retriveStatus like:p2 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter(dataId,"%"+retriveStatus+"%"));
		}
		 //全空
		 else if((dataId==null | "".equals(dataId)) & (timeCondBegin==null | "".equals(timeCondBegin))
				 & (retriveStatus == null | "".equals(retriveStatus) )){
			 sql+="order by id desc";
			 return this.find(page,sql);
		 }
		 // d 空  t 空   r 不空
		 else if((dataId==null | "".equals(dataId)) & (timeCondBegin==null | "".equals(timeCondBegin))
				 & (retriveStatus != null & !"".equals(retriveStatus) ) ){
			 sql += "where retriveStatus like:p1 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter("%"+retriveStatus+"%"));
		 }
		 //d 空   t 不空   r 空
		 else if((dataId==null | "".equals(dataId)) & (timeCondBegin!=null & !"".equals(timeCondBegin))
				 & (retriveStatus == null | "".equals(retriveStatus) )){
			 sql += "where timeCondBegin like:p1 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter("%"+timeCondBegin+"%"));
		 }
		 // d 空  t不空  r 不空  
		 else if((dataId==null | "".equals(dataId)) & (timeCondBegin!=null & !"".equals(timeCondBegin))
				 & (retriveStatus != null & !"".equals(retriveStatus) ) ){
			 sql += "where timeCondBegin like:p1 and retriveStatus like:p2 ";
			 sql+="order by id desc";
			 return this.find(page,sql,new Parameter("%"+timeCondBegin+"%","%"+retriveStatus+"%"));
		 }else {			 
			 sql+="order by id desc";
			 return this.find(page,sql);
		 }
		 
	 }
}
