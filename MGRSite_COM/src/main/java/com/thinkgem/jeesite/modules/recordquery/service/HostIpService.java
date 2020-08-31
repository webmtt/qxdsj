package com.thinkgem.jeesite.modules.recordquery.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.recordquery.dao.HostIpDao;
import com.thinkgem.jeesite.modules.recordquery.entity.HostIp;

@Service
public class HostIpService {
	@Autowired	
	HostIpDao hostIpDao;
	@Resource(name = "hostIpDao")
	public void setHostIpDao(HostIpDao hostIpDao) {
		this.hostIpDao = hostIpDao;
	}
	@Transactional
	public List<HostIp> findAll(){
		return hostIpDao.findAll();
	}
}
