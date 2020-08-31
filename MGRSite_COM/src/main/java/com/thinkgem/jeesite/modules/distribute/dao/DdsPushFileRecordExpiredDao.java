package com.thinkgem.jeesite.modules.distribute.dao;


import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushFileRecordExpired;
import org.springframework.stereotype.Repository;

@Repository
public class DdsPushFileRecordExpiredDao extends BaseDao<DdsPushFileRecordExpired> {
	
	public Page<DdsPushFileRecordExpired> getDdsPushFileRecordExpiredPage(Page<DdsPushFileRecordExpired> page,
                                                                          DdsPushFileRecordExpired ddsPushFileRecordExpired, String ftpId, String fileName, String pushStatus ) {
		String sql = " from  DdsPushFileRecordExpired  ";
		page.setOrderBy(" id desc");
		//全不空
		 if(ftpId != null & !"".equals(ftpId) & fileName != null & !"".equals(fileName)
					& pushStatus !=null & !"".equals(pushStatus)  ){
				 sql += " where ftpId =:p1 and fileName like:p2 and pushStatus like:p3 ";
				 //sql +=" order by id desc";
				 return this.find(page,sql,new Parameter(ftpId,"%"+fileName+"%","%"+pushStatus+"%"));
			 }
			 // d 不空  t 空  r 空
			 else if((ftpId !=null & !"".equals(ftpId)) & (fileName==null | "".equals(fileName))
					 & (pushStatus ==null | "".equals(pushStatus)) ){
				 sql += "where ftpId =:p1 ";
				 //sql+="order by id desc";
				 return this.find(page,sql,new Parameter(ftpId));
			 }
			// d 不空  t 不空  r 空
			 else if((ftpId !=null & !"".equals(ftpId)) & (fileName!=null & !"".equals(fileName))
					 & (pushStatus ==null | "".equals(pushStatus)) ){
				 sql += "where ftpId =:p1 and fileName like:p2 ";
				 //sql+="order by id desc";
				 return this.find(page,sql,new Parameter(ftpId,"%"+fileName+"%"));
			 }
			// d 不空  t 空  r 不空
			 else if((ftpId !=null & !"".equals(ftpId)) & (fileName==null | "".equals(fileName))
							 & (pushStatus !=null & !"".equals(pushStatus)) ){
				 sql += "where ftpId =:p1 and pushStatus like:p2 ";
				 //sql+="order by id desc";
				 return this.find(page,sql,new Parameter(ftpId,"%"+pushStatus+"%"));
			}
			 //全空
			 else if((ftpId==null | "".equals(ftpId)) & (fileName==null | "".equals(fileName))
					 & (pushStatus ==null | "".equals(pushStatus)) ){
				// sql+=" order by id ";
				 return this.find(page,sql,null);
			 }
			 // d 空  t 空   r 不空
			 else if((ftpId==null | "".equals(ftpId)) & (fileName==null | "".equals(fileName))
					 & (pushStatus !=null & !"".equals(pushStatus)) ){
				 sql += "where pushStatus like:p1 ";
				// sql+="order by id desc";
				 return this.find(page,sql,new Parameter("%"+pushStatus+"%"));
			 }
			 //d 空   t 不空   r 空
			 else if((ftpId==null | "".equals(ftpId)) & (fileName!=null & !"".equals(fileName))
					 & (pushStatus ==null | "".equals(pushStatus)) ){
				 sql += "where fileName like:p1 ";
				// sql+="order by id desc";
				 return this.find(page,sql,new Parameter("%"+fileName+"%"));
			 }
			 // d 空  t不空  r 不空  
			 else if((ftpId==null | "".equals(ftpId)) & (fileName!=null & !"".equals(fileName))
					 & (pushStatus !=null & !"".equals(pushStatus))  ){
				 sql += "where fileName like:p1 and pushStatus like:p2 ";
				// sql+="order by id desc";
				 return this.find(page,sql,new Parameter("%"+fileName+"%","%"+pushStatus+"%"));
			 }else {			 
				 //sql+=" order by id desc";
				 return this.find(page,sql,null);
			 }
			 
		 }

}
