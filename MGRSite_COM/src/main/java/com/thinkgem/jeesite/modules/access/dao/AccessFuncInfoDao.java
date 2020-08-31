package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccessFuncInfoDao extends BaseDao<AccessFuncInfo> {
	public List<AccessFuncInfo> findAll() {
		return this.find("from AccessFuncInfo");
	}

	public List<AccessFuncInfo> findByDate(String date, String showType) {
		List<AccessFuncInfo> list=null;
		if(showType!=null&&!"".equals(showType)){
			list= this.find("from AccessFuncInfo where accessdate=:p1 and sourceType=:p2", new Parameter(date,showType));
		}else{
			list= this.find("from AccessFuncInfo where accessdate=:p1", new Parameter(date));
		}
		
		return list;
	}
	public String findMaxDate(String date) {
		Session session = this.getSession();
		Query query = session
				.createSQLQuery("select max(ACCESSTIME) from STAT_ACCESSFUNCINFO where accessdate=?");
		query.setString(0, date);
		List<Object> list = query.list();
		if(list!=null&&list.size()>0){
			return (String)list.get(0);
		}
		return "0";
	}
	public List<?> findBetweenDate(String date1, String date2,String showType) {
		Session session = this.getSession();
		Query query = null;
		if(showType!=null&&!"".equals(showType)){
			query=session
					.createSQLQuery("select substr(ACCESSDATE,5), sum(pvnumber) pvsum,SUM(IPNUM) ipsum, sum(STAYTIME * pvnumber) stayTimeSum from STAT_ACCESSFUNCDATEINFO where SOURCETYPE=? and ACCESSDATE BETWEEN ? and ? GROUP BY ACCESSDATE ORDER BY ACCESSDATE");
			query.setString(0, showType);
			query.setString(1, date1);
			query.setString(2, date2);
		}else{
			query=session
					.createSQLQuery("select substr(ACCESSDATE,5), sum(pvnumber) pvsum,SUM(IPNUM) ipsum, sum(STAYTIME * pvnumber) stayTimeSum from STAT_ACCESSFUNCDATEINFO where ACCESSDATE BETWEEN ? and ? GROUP BY ACCESSDATE ORDER BY ACCESSDATE");
			query.setString(0, date1);
			query.setString(1, date2);
		}
			
		List<?> list = query.list();
		return list;
	}

	public List<?> findBetweenDate1(String date1, String date2) {
		Session session = this.getSession();
		Query query = session
				.createSQLQuery("select substr(ACCESSDATE,5),  pvsum,SUM(IPNUM) ipsum, sum(STAYTIME) stayTimeSum from STAT_ACCESSFUNCDATEINFO where ACCESSDATE BETWEEN ? and ? GROUP BY ACCESSDATE ORDER BY ACCESSDATE");
		query.setString(0, date1);
		query.setString(1, date2);
		List<?> list = query.list();
		return list;
	}
	/*
	 * public List<AccessFuncInfo> findTopTen(String date) { Session session = this.getSession(); Query query =
	 * session.createSQLQuery("SELECT * from(select * from STAT_ACCESSFUNCINFO where ACCESSDATE=? ORDER BY pvnumber DESC)where ROWNUM<=10"); query.setString(0, date); return query.list(); }
	 * 
	 * public List<AccessFuncInfo> findTopTen(String date1, String date2) { return null; }
	 */

	public List<AccessFuncInfo> findtoppv() {
		/* 查询最近的日期 */
		Session session = this.getSession();
		Query query1 = session.createSQLQuery("SELECT max(accessdate) from STAT_ACCESSFUNCINFO");
		String date1 = (String) query1.list().get(0);
		/* 查询最近的小时数 */
		Query query2 = session.createSQLQuery("select max(accesstime) from STAT_ACCESSFUNCINFO where accessdate=?");
		query2.setString(0, date1);
		String date2 = (String) query2.list().get(0);
		/* 查询最近小时的访问信息 */
		Page<AccessFuncInfo> page = new Page<AccessFuncInfo>();
		page.setPageNo(1);
		page.setPageSize(10);
		page = this.find(page, "from AccessFuncInfo where accessdate=:p1 and accesstime=:p2 order by pvnumber desc", new Parameter(date1, date2));
		return page.getList();
	}

	public int getStatisticPVNum(String date,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query = session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCINFO where ACCESSDATE = ?"+"and SOURCETYPE = ?");
			query.setString(0, date);
			query.setString(1, showType);
		}else{
			query = session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCINFO where ACCESSDATE = ?");
			query.setString(0, date);
		}
		
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;
	}

	public int getStatisticPVNum(String date, String date2,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query = session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCDATEINFO where ACCESSDATE BETWEEN ? and ?"+"and SOURCETYPE = ?");
			query.setString(1, date);
			query.setString(0, date2);
			query.setString(2, showType);
		}else{
			query = session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCDATEINFO where ACCESSDATE BETWEEN ? and ?");
			query.setString(1, date);
			query.setString(0, date2);
		}
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;
	}

	public int getStatisticPVNum(String showType) {
		Session session = this.getSession();
		System.out.println("jinlai:"+showType);
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query= session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCDATEINFO where SOURCETYPE= ?");
			query.setString(0, showType);
		}else{
			query= session
					.createSQLQuery("select sum(PVNUMBER) from STAT_ACCESSFUNCDATEINFO");

		}
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;
	}
}