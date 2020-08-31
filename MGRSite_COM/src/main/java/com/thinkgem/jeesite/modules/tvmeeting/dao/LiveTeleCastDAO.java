/*
 * @(#)LiveTeleCastDAO.java 2016年1月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年1月27日
 */
@Repository
public class LiveTeleCastDAO extends BaseDao<PlanDefine>{

	public List<PlanDefine> getTeleList() {		
		return this.find("from PlanDefine order by MeetingDate asc");
	}

	public boolean saveTele(String meetingDate, String beginTime, String endTime, String content) {
		String MeetingId=meetingDate+beginTime+endTime;
		String sql="insert into PlanDefine(MeetingId,MeetingDate,BeginTime,EndTime,Content) values(:p1,:p2,:p3,:p4,:p5)";
		int i=this.update(sql, new Parameter(MeetingId,meetingDate,beginTime,endTime,content));
		return false;
	}
	public void delTele(String meetingId) {
		this.update("delete from PlanDefine where MeetingId='"+meetingId+"'");		
	}

	public void updateTel(String meetingId, String meetingDate, String beginTime, String endTime, String content) {
		String sql="update PlanDefine set MeetingDate=:p1,BeginTime=:p2,EndTime=:p3,Content=:p4 where MeetingId=:p5";
		this.update(sql, new Parameter(meetingDate,beginTime,endTime,content));		
	}

	public PlanDefine getTeleById(String meetingId) {
		List<PlanDefine> pList=new ArrayList<PlanDefine>();
		String sql="from PlanDefine where MeetingId=:p1";
		pList=this.find(sql, new Parameter(meetingId));
		return pList.get(0);
	}

	public void saveupdate(String meetingId, String meetingDate, String beginTime, String endTime, String content,
			Date date, String updatedBy) {
		String sql="update PlanDefine set MeetingDate=:p1,BeginTime=:p2,EndTime=:p3,Content=:p4,Updated=:p5,UpdatedBy=:p6 where MeetingId=:p7";
		this.update(sql, new Parameter(meetingDate,beginTime,endTime,content,date,updatedBy,meetingId));
	}
	public void saveAdd(String meetingId, String meetingDate, String beginTime, String endTime, String content,
			String created, String createdBy) {
		String sql="insert into PlanDefine(MeetingId,MeetingDate,BeginTime,EndTime,Content,Created,CreatedBy) values(:p1,:p2,:p3,:p4,:p5,:p6,:p7)";
		this.find(sql, new Parameter(meetingId,meetingDate,beginTime,endTime,content,created,createdBy));
	}
	@Override
	public void save(PlanDefine entity) {
		super.save(entity);
	}

	public List<PlanDefine> selectByCondition(String MeetingDate, String beginTime) {
		String sql="from PlanDefine where MeetingDate=:p1 and BeginTime=:p2";		
		return this.find(sql, new Parameter(MeetingDate,beginTime));
	}
	
	public List<PlanDefine> findBeginTime(String MeetingDate){
		String sql="from PlanDefine where MeetingDate=:p1";
		return this.find(sql,new Parameter(MeetingDate));
	}

	public Page<PlanDefine> getByPage(Page<PlanDefine> page, String begin, String end) {
		Page<PlanDefine> p=new Page<PlanDefine>();
		StringBuffer sb = new StringBuffer("from PlanDefine where 1=1 ");
		if(begin!= null && !"".equals(begin)&&end!= null && !"".equals(end)){
			sb.append(" and MeetingDate >=:p1 and MeetingDate<=:p2 order by MeetingDate desc");
			p=this.find(page,sb.toString(),new Parameter(begin,end));
		}else{			
			sb.append(" order by MeetingDate desc");
			p=this.find(page, sb.toString());
		}
		return p;
	}

	public List<PlanDefine> getByCondition(String meetingId) {
		String sql="from PlanDefine where MeetingId=:p1";
		return this.find(sql, new Parameter(meetingId));
	}

	public List<PlanDefine> getTeleByDateandTime(String str, String beginTime, String endTime) {
		String sql="from PlanDefine where MeetingDate in ("+str+") and BeginTime>=:p1 and EndTime<=:p2";
		return this.find(sql,new Parameter(beginTime,endTime));
	}
	

	public void saveList(List<PlanDefine> entityList) {
		super.save(entityList);
	}
}
