/*
 * @(#)GroundDataService.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.HostSubjectDao;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.HostSubject;

/**
 * 描述：
 * 
 * @author pw
 * @version 1.0 2015-9-15
 */
@Component
@Transactional(readOnly = true)
@Service
public class HostSubjectService extends BaseService {
	@Autowired
	private HostSubjectDao hostsubjectdao;

	public List<HostSubject> getHostSubjectList(String stringValue, String url,String ctxRoot, String area) {

		List<HostSubject> hostsubjectlist = hostsubjectdao.getHostSubjectList(area);
		for (HostSubject hsj : hostsubjectlist) {
			if (hsj.getImageurl() == null) {
				hsj.setImageurl("");
			} else {
				hsj.setImageurl(hsj.getImageurl().trim());
			}
			if (hsj.getLinkurl() == null) {
				hsj.setLinkurl("");
			} else {
				hsj.setLinkurl(hsj.getLinkurl().trim());
			}
			if ((!hsj.getImageurl().startsWith("http"))&& (!hsj.getImageurl().equals(""))) {
				hsj.setImageurl(stringValue + hsj.getImageurl());
			}
			if(hsj.getLinkurl().indexOf("<ctx>")!=-1){
				hsj.setLinkurl(hsj.getLinkurl().replace("<ctx>", ctxRoot));
			}/*else
			if ((!hsj.getLinkurl().startsWith("http"))&& (!hsj.getLinkurl().equals(""))) {
				hsj.setLinkurl(url + hsj.getLinkurl());
				;
			}*/
		}
		return hostsubjectlist;
	}
	@Transactional(readOnly = false)
	public void  DelHost(Integer id,String area){
		HostSubject host=hostsubjectdao.DelHost(id);
		//host.setAreaItem(host.getAreaItem().replace(","+area+",",""));
		String areas=host.getAreaItem();
		String arr=areas.replace(area+",", "");
		host.setAreaItem(arr);
		hostsubjectdao.save(host);
	}
	 @Transactional(readOnly = false)
	public void SaveHost(HostSubject host){
		hostsubjectdao.save(host);
	}
	public List<HostSubject> find(String area){
		return hostsubjectdao.getHostSubjectList(area);
	}
	 @Transactional(readOnly = false)
	 public void SaveCol(HostSubject host){
		 hostsubjectdao.save(host);
	 }
	 public HostSubject findByHost(Integer id){
		 return  hostsubjectdao.findByID(id);
	 }	 
	 public void DelById(int ID){
		  hostsubjectdao.delById(ID);
		}
	 public void updateHost(int orderno,  int i,Integer ID) {
		 hostsubjectdao.updateTel(orderno, i, ID);
		}
	 
}
