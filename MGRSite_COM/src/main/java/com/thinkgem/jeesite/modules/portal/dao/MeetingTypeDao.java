package com.thinkgem.jeesite.modules.portal.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.portal.entity.MeetingType;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Dao
 * <p>功能描述:会议分类后台管理Dao</p>
 * @author Chen Wen
 */

@SuppressWarnings("unchecked")
@Repository("meetingTypeDao")
public class MeetingTypeDao extends BaseDao {
	
//	//添加会议分类信息
//	public void addMeeting(MeetingType meetingType){
//		try {
//			hibernateTemplate.save(meetingType);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//修改会议分类信息
//	public void update(MeetingType meetingType){
//		hibernateTemplate.update(meetingType);
//	}
//	
//	//删除会议分类信息
//	public void delete(long id){
//		try {
//			 deleteByID(MeetingType.class, "id", id);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	//展示会议分类信息列表
	public List<MeetingType> show(){
		String hql = "from MeetingType ";
		return this.find(hql);
		
	}
//	
//	//根据id查询会议分类信息（一条记录）
//	public MeetingType getMeetingTypeById(long id){
//		return hibernateTemplate.get(MeetingType.class, id);
//	}
//
	}
