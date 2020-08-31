package com.thinkgem.jeesite.modules.portal.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.portal.entity.MeetingInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao
 * <p>功能描述:会议基本信息后台管理Dao</p>
 * @author Chen Wen
 */
@SuppressWarnings("unchecked")
@Repository("meetingInfoManageDao")
public class MeetingInfoManageDao extends BaseDao {
	

		
		//删除会议基本信息
	public void deleteById(int id){
		//update("delete from MeetingInfo where id=:p1",new Parameter(id));
		update("update MeetingInfo set invalid=1 where id=:p1",new Parameter(id));
	}

	//修改会议信息
	public void update(String name,int id,long tid,String startDate,String endDate,String place,String host,String organizer,String participants,String abstracts,String thumbnail){
		//System.out.println("update DocDef set docName='"+docName+"' where docId="+docId+"");
		update("update MeetingInfo set name='"+name+"',tid='"+tid+"',startDate='"+startDate+"',endDate='"+endDate+"',place='"+place+"',host='"+host+"',organizer='"+organizer+"',participants='"+participants+"',abstracts='"+abstracts+"',thumbnail='"+thumbnail+"' where id="+id+"");
	}
	
	
	//展示会议信息列表
	 public List<MeetingInfo> show(){
			String hql = "from MeetingInfo m where m.invalid =0 order by m.startDate DESC";
			List<MeetingInfo> list = this.find(hql);
			if(list != null && list.size() > 0){
	//			for (MeetingInfo meetingInfo : list) {
	//				meetingInfo.setTime(meetingInfo.getStartDate()+"至"+meetingInfo.getEndDate());
	//			}
				return list;
			}
			return null;
		}
	 
	//根据id查询会议信息（一条记录）
		public MeetingInfo getMeetingInfoById(int id){
			Session session = this.getSession();
			String hql = "from MeetingInfo m where m.id=?";
			Query query = session.createQuery(hql);
			query.setInteger(0, id);
			List<MeetingInfo> list=query.list();
			return list.get(0);
		}
		
	 //增加一条会议记录
		public void save(MeetingInfo entity){
			super.save(entity);
		}
		
		
		public List<MeetingInfo> getMeetingInfoOneList(String title){
			Session session = this.getSession();
			String hql = "from MeetingInfo where thumbnail=?";
			Query query = session.createQuery(hql);
			//System.out.println("dao+"+title);
			query.setString(0, title);
			List<MeetingInfo> list=query.list();
			return list;
		}
		
		
		
		
}
