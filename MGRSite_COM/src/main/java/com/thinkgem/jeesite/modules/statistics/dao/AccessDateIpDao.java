package com.thinkgem.jeesite.modules.statistics.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.statistics.entity.AccessDateIP;

@Repository
public class AccessDateIpDao extends BaseDao<AccessDateIP> {
	public List<AccessDateIP> findByDate(String date) {
		return this.find("from AccessDateIP where accessdate=:p1", new Parameter(date));
	}
	/**
	 * 分省  查询日的数据
	 * @param date   选择时间的日
	 * @return
	 */
	public List<Object[]> findDataByDate(String date) {
		Session session = this.getSession();
		Query query = session.createSQLQuery("select a.proname,sum(b.ipnum),sum(pvnum) from PORTAL_IPINFOR a,test.STAT_ACCESSDATEIP b " +
				"WHERE a.ipinfor=b.ipsection and a.invalid=0 and accessdate = ? And sourcetype='province' GROUP BY a.proname order by sum(b.ipnum) desc");
		query.setString(0, date);
		List<Object[]> list = query.list();
		return list;
	}
	/**
	 * 总量和逐月的数据
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByPro(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("6")){
			hql = "Select accessdate,pvnum,ipnum From test.STAT_ACCESSDATEIP Where funitemid Is Null And accessdate>=? And accessdate<=? And length(accessdate)=6 order by accessdate  "+orderType;
		}else if(timeType.equals("8")) {
			hql = "Select accessdate,pvnum,ipnum From test.STAT_ACCESSDATEIP Where funitemid Is Null And accessdate>=? And accessdate<=? And length(accessdate)=8 order by accessdate "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		//query.setString(2, orderType);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	/**
	 * 分省
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTime(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType,String province) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//一天  、 一个月
		if(timeType.equals("8")){
			hql = " Select Source,Sum(Ipnum), Sum(Pvnum)  From test.Stat_Accessdateip Where  Accessdate>=? and Accessdate<=? And Sourcetype = 'province' And length(accessdate)=8 Group By Source";
		}else if(timeType.equals("1")) {
			//日
			hql = " Select Accessdate,Sum(Ipnum), Sum(Pvnum)  From test.Stat_Accessdateip Where Accessdate>=? and Accessdate<=? And Source=?  Group By Accessdate order by Accessdate "+orderType;
		}else if(timeType.equals("2")) {
			//月
			start_time=start_time.substring(0,6);
			hql = " Select Accessdate,Sum(proipnum), Sum(propvnum)  From test.Stat_Accessdatedata Where Accessdate>=? and Accessdate<=? And org=? And funitemid Is Null And length(accessdate)=6 Group By Accessdate order by Accessdate "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(!timeType.equals("8")){
			query.setString(2, province);
		}
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	//大院
	public List<Object[]> getSumByTimeOG(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//一天  、 一个月
		if(timeType.equals("8")){
			hql = "Select '大院' As Source,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEIP Where accessdate=? And sourcetype='center'";
		}else if(timeType.equals("0")) {
			start_time=start_time.substring(0,6);
			hql = "Select '大院' As Source,Sum(cenipnum), Sum(cenpvnum)  From test.Stat_Accessdatedata Where Accessdate =?  And org In(Select proname From PORTAL_IPINFOR Where sourcetype='center' Group By proname) And funitemid Is Null And length(accessdate)=6 ";
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
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
	public List<Object[]> getSumByTimeOrg(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType,String province) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//一天  、 一个月
		if(timeType.equals("8")){
			hql = " Select Source,Sum(Ipnum), Sum(Pvnum)  From test.Stat_Accessdateip Where  Accessdate>=? and Accessdate<=? And Sourcetype = 'center' And length(accessdate)=8 Group By Source";
		}else if(timeType.equals("1")) {
			//日
			hql = " Select Accessdate,Sum(Ipnum), Sum(Pvnum)  From test.Stat_Accessdateip Where Accessdate>=? and Accessdate<=? And Source=?  Group By Accessdate order by Accessdate "+orderType;
		}else if(timeType.equals("2")) {
			//月
			start_time=start_time.substring(0,6);
			hql = " Select Accessdate,Sum(cenipnum), Sum(cenpvnum)  From test.Stat_Accessdatedata Where Accessdate>=? and Accessdate<=? And org=? And funitemid Is Null And length(accessdate)=6 Group By Accessdate order by Accessdate "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(!timeType.equals("8")){
			query.setString(2, province);
		}
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
}
