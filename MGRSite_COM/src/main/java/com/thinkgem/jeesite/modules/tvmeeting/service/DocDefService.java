/*
 * @(#)DocDefService.java 2016-1-27
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.tvmeeting.dao.DocDefDao;
import com.thinkgem.jeesite.modules.tvmeeting.entity.DocDef;

/**
 * 描述：
 *
 * @author huanglei
 * @version 1.0 2016-1-27
 */
@Service
public class DocDefService {
	@Autowired
	private DocDefDao docDefDao;
	
	@Transactional
	public void save(DocDef docDef){
		docDefDao.save(docDef);
	}
	
	@Transactional
	public void deleteById(String id){
		docDefDao.deleteById(id);
	}
	
	@Transactional
	public void findAll(){
		docDefDao.findAll();
	}
	
	public Page<DocDef> findDocDefPage(Page<DocDef> page,DocDef docDef){
		return docDefDao.findDocDefPage(page, docDef);
	} 
	
	public DocDef findDocDef(String docId){
		return docDefDao.findDocDef(docId);
	}
	
	@Transactional
	public void update(String docName,String docId){
		docDefDao.update(docName,docId);
	}
	public List<DocDef> findDocDefList(String docId){
		return docDefDao.findDocDefList(docId);
		
	}
}
