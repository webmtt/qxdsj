package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.access.dao.PortalIPInforDao;
import com.thinkgem.jeesite.modules.access.entity.PortalIPInfor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortalIPInforService extends BaseService {
	@Autowired
	private PortalIPInforDao portalipinfordao;
	public List<PortalIPInfor> findAll(){
		return this.portalipinfordao.findAll();
	}
}
