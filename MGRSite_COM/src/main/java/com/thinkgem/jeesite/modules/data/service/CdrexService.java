package com.thinkgem.jeesite.modules.data.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.CdrexDao;
import com.thinkgem.jeesite.modules.data.entity.Cdrex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 国家字典表Service
 * 
 * @author yb
 * @version 2014-10-30
 */
@Component
@Transactional(readOnly = true)
public class CdrexService extends BaseService {
	@Autowired
	private CdrexDao phoneStatisticsDao;
    
	public Cdrex getPhoneStatistics(String id){
		phoneStatisticsDao.get(id);
		return null;
		
	}
	public Map<String, Integer> getCountOfCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		return this.phoneStatisticsDao.getCountOfCall(startYear, endYear, startMonth, endMonth, groupType, type);
	}
	public Map<String, Integer> getCountOfMissCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		return this.phoneStatisticsDao.getCountOfMissCall(startYear, endYear, startMonth, endMonth, groupType, type);
	}
	public Map<String, Float> getCountReceiveCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		return this.phoneStatisticsDao.getCountReceiveCall(startYear, endYear, startMonth, endMonth, groupType, type);
	}
	public List<Cdrex> find(String begin){
		List<Cdrex> lists=phoneStatisticsDao.find(begin);
		
		return lists;
	}
	public int getphoneNumByRange(String startTime,String endTime) {
		return this.phoneStatisticsDao.getphoneNumByRange(startTime,endTime);
	}
	public int getphoneNum(String dateStr) {
		return this.phoneStatisticsDao.getphoneNum(dateStr);
	}
	
	public Page<Cdrex> getByPage(Page<Cdrex> page){
		return phoneStatisticsDao.getByPage(page);
	}
	public Map<String, Integer> getIncomingCall(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getIncomingCall(startTime, endTime, type);
	}
	public Map<String, Integer> getIncomingCall2(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getIncomingCall2(startTime, endTime, type);
	}
	public Map<String, Float> getIncomingCall3(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getIncomingCall3(startTime, endTime, type);
	}
	public List<Object[]> getList(String startTime, String endTime) {
		return phoneStatisticsDao.getList(startTime, endTime);
	}
	public List<Object[]> getAllTime(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getAllTime(startTime, endTime,type);
	}
	public List<Object[]> getReceive(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getReceive(startTime, endTime,type);
	}
	public List<Object[]> getCallLoss(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getCallLoss(startTime, endTime,type);
	}
	public List<Object[]> getExhale(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getExhale(startTime, endTime,type);
	}
	public List<Object[]> getNoReceive(String startTime, String endTime,String type) {
		return phoneStatisticsDao.getNoReceive(startTime, endTime,type);
	}
}
