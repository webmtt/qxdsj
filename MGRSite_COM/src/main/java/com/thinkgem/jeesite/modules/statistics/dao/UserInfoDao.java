/*
 * @(#)UserInfoDAO.java 2016年3月16日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.statistics.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Repository
public class UserInfoDao extends BaseDao<UserInfo>{
	/**
	 * 总量 查询
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTime(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//年月日  对应着4，6，8
		if(timeType.equals("8")){
			hql="Select a.* From (Select date_format(created,'%Y%m%d') As times,Count(*) From Sup_Userinfo Where created>=str_to_date(?,'%Y%m%d') "+
                "And created<=str_to_date(?,'%Y%m%d') and created Is Not Null Group By date_format(created,'%Y%m%d')) a Order By a.times "+orderType;
		}else if(timeType.equals("6")) {
			hql="Select a.* From (Select date_format(created,'%Y%m') As times,Count(*) From Sup_Userinfo Where created>=str_to_date(?,'%Y%m%d') "+
	                "And created<=str_to_date(?,'%Y%m%d') and created Is Not Null Group By date_format(created,'%Y%m')) a Order By a.times "+orderType;
		}else if(timeType.equals("4")) {
			hql="Select a.* From (Select date_format(created,'%Y') As times,Count(*) From Sup_Userinfo Where created>=str_to_date(?,'%Y%m%d') "+
	                "And created<=str_to_date(?,'%Y%m%d') and created Is Not Null Group By date_format(created,'%Y')) a Order By a.times "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	public List<String> statYears(){
		String sql="Select date_format(created,'%Y') As times From Sup_Userinfo Where  Delflag = '0' And created Is Not Null  Group By date_format(created,'%Y')";
		List list=this.findBySql(sql);
		return list;
		
	}
	/**
	 * 分单位
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTimeOrg(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//一天  、 一个月
		if(timeType.equals("8")){
			hql="Select Name,num  From  (Select substr(a.orgid,0,6) As org, Count(*) As num From test.Sup_Userinfo a Left Join Sup_Orginfo b On a.Orgid = b.Id "+
			"Where a.Created >= To_Date(?, 'yyyymmdd') And a.Created <= To_Date(?, 'yyyymmdd') And Delflag = '0' And (b.Name Not Like '%气象局' Or b.Name='中国气象局') "+
			"Group By substr(a.orgid,0,6) Order By substr(a.orgid,0,6)) c Left Join Sup_Orginfo d On c.org=d.Id Where (d.Name Not  Like '%气象局' Or d.Name='中国气象局') order by num "+orderType;
		}else if(timeType.equals("0")) {
			
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	/**
	 * 分单位
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTimePro(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//一天  、 一个月
		if(timeType.equals("8")){
			hql="Select Name,num  From  (Select substr(a.orgid,0,6) As org, Count(*) As num From test.Sup_Userinfo a Left Join Sup_Orginfo b On a.Orgid = b.Id "+
			"Where a.Created >= To_Date(?, 'yyyymmdd') And a.Created <= To_Date(?, 'yyyymmdd') And Delflag = '0' And (b.Name  Like '%气象局' And  b.Name!='中国气象局') "+
			"Group By substr(a.orgid,0,6) Order By substr(a.orgid,0,6)) c right Join (Select Id,Name From Sup_Orginfo a  Where parentid='001' And a.Name  Like '%气象局' And  a.Name!='中国气象局') d On c.org=d.Id order by num ";
		}else if(timeType.equals("0")) {
			
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	
}
