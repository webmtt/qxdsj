package com.thinkgem.jeesite.modules.portal.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.portal.entity.MeetingDetailInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("meetingDetailInfoManageDao")
public class MeetingDetailInfoManageDao extends BaseDao {

		
		//展示会议信息列表
		public List<MeetingDetailInfo> show(){
			String hql = "from MeetingDetailInfo m where m.invalid=0 and m.date is not null and m.startTime is not null ";
			List<MeetingDetailInfo> list = this.find(hql);
			if(list != null && list.size() > 0){
				for (MeetingDetailInfo meetingInfo : list) {
					meetingInfo.setTime(meetingInfo.getStartTime()+"-"+meetingInfo.getEndTime());
				}
				return list;
			}
			return null;
		}
		//增加
		public void save(MeetingDetailInfo entity){
			super.save(entity);
		}
		//删除精彩议题
		public void deleteById(int id){
			//update("delete from MeetingInfo where id=:p1",new Parameter(id));
			update("update MeetingDetailInfo set invalid=1 where id=:p1",new Parameter(id));
		}
		
		//修改会议信息
		public void update(int id,String date,String unit,String content,String spokesman,String filename,String starttime,String endtime){
			update("update MeetingDetailInfo set date='"+date+"',unit='"+unit+"',content='"+content+"',spokesman='"+spokesman+"',filename='"+filename+"',starttime='"+starttime+"',endtime='"+endtime+"' where id="+id+"" );	
		}
		
		//根据pid查询会议详细信息
		public List<MeetingDetailInfo> getMeetingDetailInfoByPid(int pid){
			Session session=this.getSession();
			String hql = "from MeetingDetailInfo m where m.pid=? and m.invalid=0 and m.date is not null and m.startTime is not null and m.endTime is not null order by m.date, TO_DATE(m.startTime, 'hh24:mi'),m.id";
			Query query=session.createQuery(hql);
			query.setLong(0, pid);
			List<MeetingDetailInfo> list = query.list();
			if(list != null && list.size() > 0){
				for (MeetingDetailInfo meetingDetailInfo : list) {
					meetingDetailInfo.setTime(meetingDetailInfo.getStartTime()+"-"+meetingDetailInfo.getEndTime());
				}
				return list;
			}
			return null;
		}
		public MeetingDetailInfo getMeetingDetailInfoById(int id){
			Session session=this.getSession();
			String hql = "from MeetingDetailInfo m where m.id=? and m.invalid=0 and m.date is not null and m.startTime is not null  order by m.date, TO_DATE(m.startTime, 'hh24:mi'),m.id";
			Query query=session.createQuery(hql);
			query.setInteger(0, id);
			List<MeetingDetailInfo> list = query.list();
			if(list != null && list.size() > 0){
				for (MeetingDetailInfo meetingDetailInfo : list) {
					meetingDetailInfo.setTime(meetingDetailInfo.getStartTime()+"-"+meetingDetailInfo.getEndTime());
				}
				return list.get(0);
			}
			return null;
		}
		
		public List<MeetingDetailInfo> getMeetingDetailInfoOneList(String filename){
			Session session = this.getSession();
			String hql = "from MeetingDetailInfo where fileName=?";
			Query query = session.createQuery(hql);
			//System.out.println("dao+"+title);
			query.setString(0, filename);
			List<MeetingDetailInfo> list=query.list();
			return list;
		}

}
