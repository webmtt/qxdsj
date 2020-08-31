package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.ObservationInfoDao;
import com.thinkgem.jeesite.modules.index.entity.ObservationInfo;


@Service
public class ObservationInfoService extends BaseService{
	@Autowired
	private ObservationInfoDao observationInfoDao;
	@Transactional
	public List<ObservationInfo> findAll() {
		return this.observationInfoDao.findData();
	}
	@Transactional
	public List<ObservationInfo> getByChnname(String chnname) {
		return this.observationInfoDao.getByChnname(chnname);
	}
	@Transactional
	public ObservationInfo findObservationInfoById(String id) {
		return this.observationInfoDao.getById(id);
	}
	@Transactional
	public void saveUpdate(ObservationInfo observationInfo) {
		this.observationInfoDao.save(observationInfo);
	}
	@Transactional
	public void deleteObservationInfo(String id) {
		this.observationInfoDao.delObservationInfo(id);
		
	}
	@Transactional
	public void saveObservationInfoData(ObservationInfo observationInfo) {
		this.observationInfoDao.saveData(observationInfo);
		
	}

}
