/*
 * @(#)DocDef.java 2016-1-27
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.tvmeeting.entity.DocDef;

/**
 * 描述：
 *
 * @author huanglei
 * @version 1.0 2016-1-27
 */
@Repository
public class DocDefDao extends BaseDao<DocDef>{

	@Override
	public void save(DocDef entity) {
		// TODO Auto-generated method stub
		super.save(entity);
	}

	public void deleteById(String id){
		update("delete from DocDef where id=:p1",new Parameter(id));
	}
	@Override
	public List<DocDef> findAll() {
		// TODO Auto-generated method stub
		return super.findAll();
	}	
	
	public DocDef findDocDef(String docId) {
		return (DocDef)this.getSession().createQuery("from DocDef where docId=?").setParameter(0, docId).list()
				.get(0);
	}
	
	public void update(String docName,String docId){
		System.out.println("update DocDef set docName='"+docName+"' where docId="+docId+"");
		update("update DocDef set docName='"+docName+"' where docId="+docId+"");
	}
	
	public Page<DocDef> findDocDefPage(Page<DocDef> page,DocDef docDef){
		String sql="from DocDef where 1=1";
		return this.find(page,sql,new Parameter());
	}
	public List<DocDef> findDocDefList(String docId){
		String sql="from DocDef where 1=1 and docId=:p1";
		return this.find(sql,new Parameter(docId));
	}
	
}
