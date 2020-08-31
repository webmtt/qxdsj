package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalPicShowDao;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.PortalPicShowDef;

@Service
public class PortalPicShowService extends BaseService {
	@Autowired
	private PortalPicShowDao protalPicShowDao;

	public List<PortalPicShowDef> getAll(String url,String ctxRoot, String area) {
		List<PortalPicShowDef> list=protalPicShowDao.getAll(area);
		for (PortalPicShowDef psd : list) {
			if(psd.getLinkURL()==null){
				psd.setLinkURL("");
			}else{
				psd.setLinkURL(psd.getLinkURL().trim());
			}
			if(psd.getLinkURL().indexOf("<ctx>")!=-1){
				psd.setLinkURL(psd.getLinkURL().replace("<ctx>", ctxRoot));
			}
		}
		return list;
	}
	@Transactional(readOnly = false)
	public void  Delpps(String picCode,String area){
		PortalPicShowDef portal=protalPicShowDao.Delpps(picCode);
		String areas=portal.getAreaItem();
		String aree=areas.replace(area+",", "");
		portal.setAreaItem(aree);
		protalPicShowDao.save(portal);
		
	}
	 @Transactional(readOnly = false)
	public void savepps(PortalPicShowDef pps){
		protalPicShowDao.save(pps);
	}
	public List<PortalPicShowDef> find(String area){
		return protalPicShowDao.getAll(area);
	}
	 public PortalPicShowDef findBypps(String picCode){
		 return protalPicShowDao.findByID(picCode);
	 }
	 public void DelById(String picCode){
		 protalPicShowDao.delById(picCode);
		}
	 public void updatapps(String chnName, int invalid, int orderNo,String picCode){
		 protalPicShowDao.updateTel(chnName, invalid, orderNo, picCode);
		}
	 
}
