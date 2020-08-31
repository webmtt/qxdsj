package com.thinkgem.jeesite.modules.recordquery.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.recordquery.dao.FtpUrlDataDao;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlData;
@Service
public class FtpUrlDataService extends BaseService{
	@Resource
	private FtpUrlDataDao fDao;
	@Transactional
	public Page<FtpUrlData> getDataByPage(Page page, String begin, String end,String sortds,String sortad) {
		return fDao.getDataByPage(  page,begin,end,sortds,sortad);
	}
	@Transactional
	public List getDownSize(String begin, String end,String showType) {
		return fDao.getDownSize(begin,end,showType);
	}
	@Transactional
	public List getPvNumber(String begin, String end,String showType) {
		return fDao.getPvNumber(begin,end,showType);
	}

	/*===========================FTP下载量统计========================================*/
	//初始化年
	@Transactional
	public List<String> getYears() {
		return fDao.getYears();
	}		
	//按总量 获取下载量
	@Transactional
	public List<FtpUrlData> getDownLoadSumByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		return fDao.getDownLoadSumByTime(start_time, end_time, timeType,page, row,orderType);
	}		
	@Transactional
	public BigDecimal getDownLoadTotalByTime(String start_time,String end_time) {
		return fDao.getDownLoadTotalByTime(start_time, end_time);
	}
	
	//按分类
	@Transactional
	public List getDownLoadSumClassByTime(String start_time,String end_time){
		return fDao.getDownLoadSumClassByTime(start_time, end_time);
	}
	
	//按分类-时间分类获取下载量(列表)
	@Transactional
	public List getDownLoadSumClassByTime(String start_time, String end_time, String timeType,
			String ordername, String ordertype, Integer page, Integer rows) {
		return fDao.getDownLoadSumClassByTime(start_time, end_time,timeType,ordername,ordertype,page,rows);
	}
	//按分类
	@Transactional
	public List getDownLoadSumClassByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType){
		return fDao.getDownLoadSumClassByTime(start_time, end_time,timeType,page,row,orderType);
	}
	@Transactional
	public List getDownLoadSumClassByTime2(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType,String org){
		return fDao.getDownLoadSumClassByTime2(start_time, end_time,timeType,page,row,orderType,org);
	}
	
	
	//按省     获取下载量
	@Transactional
	public List<FtpUrlData> getDownLoadSumProvinceByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		return fDao.getDownLoadSumProvinceByTime(start_time, end_time, timeType,page, row,orderType);
	}
	@Transactional
	public List<FtpUrlData> getDownLoadSumProvinceByTime2(String start_time, String end_time, String timeType,
			Integer page,Integer row,String orderType) {
		return fDao.getDownLoadSumProvinceByTime2(start_time, end_time, timeType,page, row,orderType);
	}
//	public FtpUrlData getDownLoadSumProvinceByTime2(String start_time, String end_time, String timeType,
//			Integer page,Integer row,String orderType) {
//		return fDao.getDownLoadSumProvinceByTime2(start_time, end_time, timeType,page, row,orderType);
//	}
	@Transactional
	public List<FtpUrlData> getDownLoadSumProvinceByTime3(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType){
		return fDao.getDownLoadSumProvinceByTime3(start_time, end_time, timeType,page, row,orderType);
	}
	
	//获取省份
	public List<String> getOrg(String orgType) {
		return fDao.getOrg(orgType);
	}
	//获取省份
	public List<String[]> getOrgAll() {
		return fDao.getOrgAll();
	}
	//根据省份，按年、月、日进行查询
	@Transactional
	public List<FtpUrlData> getDownLoadSumProvinceByTime(String start_time, String end_time, String timeType,
			String org, Integer page,Integer row, String orderType) {
		return fDao.getDownLoadSumProvinceByTime(start_time, end_time, timeType,org,page, row,orderType);
	}
	
	
	@Transactional
	public BigDecimal getDownLoadProvinceByTime(String start_time,String end_time) {
		return fDao.getDownLoadProvinceByTime(start_time, end_time);
	}
	//按 大院用户机构 获取下载量
	@Transactional
	public List<FtpUrlData> getDownLoadSumCenterByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		return fDao.getDownLoadSumCenterByTime(start_time, end_time, timeType,page, row,orderType);
	}
	@Transactional
	public List<FtpUrlData> getDownLoadSumCenterByTime2(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType){
		return fDao.getDownLoadSumCenterByTime2(start_time, end_time, timeType,page, row,orderType);
	}
	@Transactional
	public List<FtpUrlData> getDownLoadSumCenterByTime(String start_time, String end_time, String timeType,String org, Integer page,Integer row, String orderType) {
		return fDao.getDownLoadSumCenterByTime(start_time, end_time, timeType,org,page, row,orderType);
	}
	
	@Transactional
	public BigDecimal getDownLoadCenterByTime(String start_time,String end_time) {
		return fDao.getDownLoadCenterByTime(start_time, end_time);
	}
	/*====================================================================================*/
	public List<Map<String,String>> getdatasizeandpvNumber(String begin, String end) {
		return fDao.getDownSizeAndPv(begin, end);
	}
	public List<Map<String,String>> getdatasizeandpvNumber2(String begin, String end) {
		return fDao.getDownSizeAndPv2(begin, end);
	}
	public List<Map<String,String>> getdatasizeandpvNumberByDept(String begin, String end) {
		return fDao.getDownSizeAndPvByDept(begin, end);
	}

}
