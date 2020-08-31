package com.thinkgem.jeesite.modules.portal.service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.portal.dao.MeetingTypeDao;
import com.thinkgem.jeesite.modules.portal.entity.MeetingType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service
 * <p>功能描述:会议分类后台管理Service</p>
 * @author Chen Wen
 */

@Service("meetingTypeService")
public class MeetingTypeService extends BaseService {
	
	/**
	 * dao注入
	 */
	@Resource(name = "meetingTypeDao")
	private MeetingTypeDao dao;
	
//	//添加会议分类信息
//	public boolean addMeeting(MeetingType meetingType){
//		boolean flag=false;
//		try {
//			dao.addMeeting(meetingType);
//			flag=true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag=false;
//		}
//		return flag;
//	}
//	
//	//修改会议分类信息
//	public boolean update(MeetingType meetingType){
//		boolean flag = false;
//		try {
//			dao.update(meetingType);
//			flag = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag = false;
//		}
//		return flag;
//	}
//	
//	//删除会议分类信息
//	public boolean delete(String idStr){
//		boolean flag = false;
//		try {
//			String[] fn = idStr.split(",");
//			for(int i=0;i<fn.length;i++){
//				long id = Long.parseLong(fn[i]);
//				dao.delete(id);
//			}
//			flag = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag = false;
//		}
//		return flag;
//	}
	
	//展示会议分类信息
	public List<MeetingType> showMeeting(){
		return this.dao.show();
	}
	
//	//获取会议分类信息（一条记录）
//	public MeetingType getMeetingType(long id){
//		return dao.getMeetingTypeById(id);
//	}
}
