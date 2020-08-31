package com.thinkgem.jeesite.modules.portal.service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.portal.dao.MeetingInfoManageDao;
import com.thinkgem.jeesite.modules.portal.entity.MeetingInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Service
 * <p>功能描述:会议基本信息后台管理Service</p>
 * @author Chen Wen
 */

@Service("meetingInfoManageService")
public class MeetingInfoManageService extends BaseService {
	
	@Resource(name = "meetingInfoManageDao")
	private MeetingInfoManageDao dao;
	
	//增加一条会议记录
	@Transactional(readOnly = false)
	 public void save(MeetingInfo entity){
		 dao.save(entity);
	 }
	//删除会议基本信息
		public void deleteById(int id){
				dao.deleteById(id);
		}
	
	//修改会议信息
		public void update(String name,int id,long tid,String startDate,String endDate,String place,String host,String organizer,String participants,String abstracts,String thumbnail){
			dao.update(name,id,tid,startDate,endDate,place,host,organizer,participants,abstracts,thumbnail);
		}
	
	public List<MeetingInfo> showMeeting(){
		return dao.show();
	}
	
	//近期会议（获取前6条会议）
	public List<MeetingInfo> recentMeeting(){
		List<MeetingInfo> list = dao.show();
		if(list !=null && list.size() > 6){
			return dao.show().subList(0, 6);
		}
		return list;
	}
	
	//获取会议信息（一条记录）
	public MeetingInfo getMeetingInfoById(int id){
		return dao.getMeetingInfoById(id);
	}
	
	public List<MeetingInfo>getMeetingInfoOneList(String title){
		return dao.getMeetingInfoOneList(title);
	}

}
