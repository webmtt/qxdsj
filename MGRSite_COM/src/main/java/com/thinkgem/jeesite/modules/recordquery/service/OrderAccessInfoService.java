package com.thinkgem.jeesite.modules.recordquery.service;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.recordquery.dao.OrderAccessInfoDao;
import com.thinkgem.jeesite.modules.recordquery.entity.OrderAccessInfo;
import com.thinkgem.jeesite.modules.recordquery.entity.OrderAccessInfoModel;

/**
 * 国家字典表Service
 * @author yb
 * @version 2014-10-30
 */
@Component
@Transactional(readOnly = true)
public class OrderAccessInfoService extends BaseService {

	@Autowired
	private OrderAccessInfoDao orderAccessInfoDao;
	public List<OrderAccessInfoModel> getSumClassByTime(String start_time,String end_time){
		return orderAccessInfoDao.getSumClassByTime(start_time, end_time);
	}
	public BigDecimal getTotalByTime(String start_time,String end_time) {
		return orderAccessInfoDao.getTotalByTime(start_time, end_time);
	}
	//下载量
	public List<OrderAccessInfoModel> getDownLoadSumClassByTime(String start_time,String end_time){
		return orderAccessInfoDao.getDownLoadSumClassByTime(start_time, end_time);
	}
	public List<OrderAccessInfo> getDownLoadSumByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		return orderAccessInfoDao.getDownLoadSumByTime(start_time, end_time, timeType,page, row,orderType);
	}	
	@Transactional
	public List getDownLoadSumClassByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType,String org){
		return orderAccessInfoDao.getDownLoadSumClassByTime(start_time, end_time,timeType,page,row,orderType,org);
	}
	public List<Object[]> getDepart(String type) {
		return orderAccessInfoDao.getDepart(type);
	}
}
