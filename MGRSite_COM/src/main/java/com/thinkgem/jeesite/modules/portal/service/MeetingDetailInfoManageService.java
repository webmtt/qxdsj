package com.thinkgem.jeesite.modules.portal.service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.portal.dao.MeetingDetailInfoManageDao;
import com.thinkgem.jeesite.modules.portal.entity.MeetingDetailInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service
 * <p>功能描述:会议基本信息后台管理Service</p>
 * @author Chen Wen
 */

@Service("meetingDetailInfoManageService")
public class MeetingDetailInfoManageService extends BaseService {
	
	/**
	 * dao注入
	 */
	@Resource(name = "meetingDetailInfoManageDao")
	private MeetingDetailInfoManageDao dao;
	
	//修改会议信息
	public void update(int id,String date,String unit,String content,String spokesman,String filename,String starttime,String endtime){
		dao.update(id ,date, unit, content, spokesman, filename,starttime,endtime);
	}
	//展示会议信息
	public List<MeetingDetailInfo> showMeeting(){
		return dao.show();
	}

	//删除一条精彩议题
	public void deleteById(int id){
		dao.deleteById(id);
	}
	
//	//获取会议信息（一条记录）
//	public MeetingDetailInfo getMeetingDetailInfo(long id){
//		return dao.getMeetingDetailInfoById(id);
//	}
//	
	//获取单个会议下的所有详细信息
	public List<MeetingDetailInfo> getMeetingDetailInfoByPid(int pid){
		return dao.getMeetingDetailInfoByPid(pid);
	}
	public MeetingDetailInfo getMeetingDetailInfoById(int id){
		return dao.getMeetingDetailInfoById(id);
	}
	
	//增加一条议题记录
	@Transactional(readOnly = false)
	public void save(MeetingDetailInfo entity){
		dao.save(entity);
	}
	
	
	public List<MeetingDetailInfo>getMeetingDetailInfoOneList(String filename){
		return dao.getMeetingDetailInfoOneList(filename);
	}
}
