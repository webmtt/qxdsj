/*
 * @(#)LiveTeleCastService.java 2016年1月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.tvmeeting.dao.LiveTeleCastDAO;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年1月27日
 */
@Service
public class LiveTeleCastService {
	@Autowired
	private LiveTeleCastDAO lcDao;
	@Transactional
	public List<PlanDefine> getTeleList() {		
		return lcDao.getTeleList();
	}
/*	public boolean saveTele(String meetingDate, String beginTime, String endTime, String content) {
		return lcDao.saveTele(meetingDate,beginTime,endTime,content);
	}*/
	/*public void saveTele(PlanDefine pd) {
		lcDao.saveTele(pd);
	}*/
	@Transactional
	public void delTele(String meetingId) {
		lcDao.delTele(meetingId);
	}
	@Transactional
	public void updateTele(String meetingId, String meetingDate, String beginTime, String endTime, String content) {
		lcDao.updateTel(meetingId,meetingDate,beginTime,endTime,content);
		
	}
	@Transactional
	public PlanDefine getTelById(String meetingId) {
		return lcDao.getTeleById(meetingId);
		
	}
	
	@Transactional
	public List<PlanDefine> findBeginTime(String meetingDate) {
		return lcDao.findBeginTime(meetingDate);
		
	}
	
	@Transactional
	public void saveupdate(String meetingId, String meetingDate, String beginTime, String endTime, String content,
			Date date, String updatedBy) {
		lcDao.saveupdate(meetingId,meetingDate,beginTime,endTime,content,date,updatedBy);
	}
	@Transactional
	public void saveAdd(String meetingId, String meetingDate, String beginTime, String endTime, String content,
			String cteateTime, String userName) {
		lcDao.saveAdd(meetingId, meetingDate, beginTime, endTime, content, cteateTime, userName);
	}
	@Transactional
	public void saveTele(PlanDefine pd) {
		lcDao.save(pd);		
	}
	public List<PlanDefine> selectByCondition(String meetingDate, String beginTime) {
		return lcDao.selectByCondition(meetingDate,beginTime);
	}
	public Page<PlanDefine> findByPage(Page<PlanDefine> page, String begin, String end) {
		return lcDao.getByPage(page,begin,end);
	}
	public List<PlanDefine> selectByCondition(String meetingId) {
		return lcDao.getByCondition(meetingId);
	}
	public List<PlanDefine> getTeleByDateandTime(String str, String beginTime, String endTime) {
		return lcDao.getTeleByDateandTime(str,beginTime,endTime);
	}
	@Transactional
	public void saveBatchTele(List<PlanDefine> pList) {
		lcDao.saveList(pList);
	}
	
}
