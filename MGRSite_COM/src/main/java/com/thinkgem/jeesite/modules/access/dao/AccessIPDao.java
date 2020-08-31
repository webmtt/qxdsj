package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.AccessIP;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccessIPDao extends BaseDao<AccessIP> {
	public List<AccessIP> findByDate(String date) {
		return this.find("from AccessIP where accessdate=:p1", new Parameter(date));
	}

	public List<Object[]> findDataByDate(String date,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query= session.createSQLQuery("select a.proname,sum(b.ipnum) from PORTAL_IPINFOR a,STAT_ACCESSIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate = ? and b.sourceType = ? GROUP BY a.proname order by sum(b.ipnum) desc");
			query.setString(0, date);
			query.setString(1, showType);
		}else{
			query= session.createSQLQuery("select a.proname,sum(b.ipnum) from PORTAL_IPINFOR a,STAT_ACCESSIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate = ? GROUP BY a.proname order by sum(b.ipnum) desc");
			query.setString(0, date);
		}
		
		List<Object[]> list = query.list();
		return list;
	}
	
	public List<Object[]> findPvDataByDate(String date,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query= session.createSQLQuery("select a.proname,sum(b.pvnum) from PORTAL_IPINFOR a,STAT_ACCESSIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate = ? and b.sourceType = ? GROUP BY a.proname order by sum(b.pvnum) desc");
			query.setString(0, date);
			query.setString(1, showType);
		}else{
			query= session.createSQLQuery("select a.proname,sum(b.pvnum) from PORTAL_IPINFOR a,STAT_ACCESSIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate = ? GROUP BY a.proname order by sum(b.pvnum) desc");
			query.setString(0, date);
		}
		
		List<Object[]> list = query.list();
		return list;
	}

	public List<Object[]> findDataByDate(String date1, String date2,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query = session.createSQLQuery("select a.proname,sum(b.ipnum) from PORTAL_IPINFOR a,STAT_ACCESSDATEIP b " + 
					"WHERE a.ipinfor=b.ipsection and b.sourceType = ? and accessdate BETWEEN ? and ? GROUP BY a.proname order by sum(b.ipnum) desc");
			query.setString(0, showType);
			query.setString(1, date1);
			query.setString(2, date2);
		}else{
			query = session.createSQLQuery("select a.proname,sum(b.ipnum) from PORTAL_IPINFOR a,STAT_ACCESSDATEIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate BETWEEN ? and ? GROUP BY a.proname order by sum(b.ipnum) desc");
			query.setString(0, date1);
			query.setString(1, date2);
		}
		
		List<Object[]> list = query.list();
		return list;
	}
	
	public List<Object[]> findPvDataByDate(String date1, String date2,String showType) {
		Session session = this.getSession();
		Query query=null;
		if(showType!=null&&!"".equals(showType)){
			query = session.createSQLQuery("select a.proname,sum(b.pvnum) from PORTAL_IPINFOR a,STAT_ACCESSDATEIP b " + 
					"WHERE a.ipinfor=b.ipsection and b.sourceType = ? and accessdate BETWEEN ? and ? GROUP BY a.proname order by sum(b.pvnum) desc");
			query.setString(0, showType);
			query.setString(1, date1);
			query.setString(2, date2);
		}else{
			query = session.createSQLQuery("select a.proname,sum(b.pvnum) from PORTAL_IPINFOR a,STAT_ACCESSDATEIP b " + 
					"WHERE a.ipinfor=b.ipsection and accessdate BETWEEN ? and ? GROUP BY a.proname order by sum(b.pvnum) desc");
			query.setString(0, date1);
			query.setString(1, date2);
		}
		
		List<Object[]> list = query.list();
		return list;
	}

	public Map<String, Object> topAddress() {
		/* 查询最近的日期 */
		Session session = this.getSession();
		Query query1 = session.createSQLQuery("SELECT max(accessdate) from STAT_ACCESSIP");
		String date1 = (String) query1.list().get(0);
		/* 查询最近的小时数 */
		Query query2 = session.createSQLQuery("select max(accesstime) from STAT_ACCESSIP where accessdate=?");
		query2.setString(0, date1);
		String date2 = (String) query2.list().get(0);
		Query query = session.createSQLQuery("SELECT * from (select a.proname,sum(b.ipnum) c from PORTAL_IPINFOR a,STAT_ACCESSIP b "
				+ "WHERE a.ipinfor=b.ipsection and accessdate =? and accesstime=? GROUP BY a.proname ORDER BY c DESC) WHERE ROWNUM<=10");
		query.setString(0, date1);
		query.setString(1, date2);
		List<Object[]> list = query.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("date1", date1);
		map.put("date2", date2);
		return map;
	}

	public int getStatisticIPNum(String date) {
		Session session = this.getSession();
		Query query = session.createSQLQuery("select sum(IPNUM) from STAT_ACCESSIP where ACCESSDATE = ?");
		query.setString(0, date);
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;
	}

	public int getStatisticIPNum(String date, String date2) {
		Session session = this.getSession();
		Query query = session.createSQLQuery("select sum(IPNUM) from STAT_ACCESSDATEIP where ACCESSDATE BETWEEN ? and ?");
		query.setString(1, date);
		query.setString(0, date2);
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;	
	}
	
	
	public List<Object[]>  getHourIPPVloadByRange(String sdate, String edate) {
		Session session = this.getSession();
		Query query = session.createSQLQuery("SELECT pv.accessdate,pv.accesstime,pv.pvnumber,ip.ipnum FROM "
				+ "( SELECT accessdate,accesstime,sum(pvnumber) AS pvnumber FROM STAT_ACCESSFUNCINFO a WHERE accessdate||accesstime >= ? AND accessdate||accesstime <= ? GROUP BY accessdate,accesstime  ) pv,"
				+ "( SELECT accessdate,accesstime,sum(ipnum) AS ipnum FROM STAT_ACCESSIP a WHERE accessdate||accesstime >= ? AND accessdate||accesstime <= ? GROUP BY accessdate,accesstime  ) ip "
				+ "WHERE pv.accessdate = ip.accessdate AND pv.accesstime = ip.accesstime   ORDER BY pv.accessdate,pv.accesstime");
		query.setString(0, sdate);
		query.setString(1, edate);
		query.setString(2, sdate);
		query.setString(3, edate);
		List<Object[]> list = query.list();
		return list;
	}
	
	

	public int getStatisticPVNum() {
		Session session = this.getSession();
		Query query = session.createSQLQuery("select sum(IPNUM) from STAT_ACCESSDATEIP");
		List list = query.list();
		int result = 0;
		if(list.get(0) != null){
			Object object = query.list().get(0);
			result = Integer.parseInt(object.toString());
		}
		return result;	
	}
}
