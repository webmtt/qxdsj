package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.AccessData;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AccessDataDao extends BaseDao<AccessData> {
	
	public List<AccessData> findByDateIPNum(String date) {
		return this.find("from AccessFuncInfo where accessdate=:p1", new Parameter(date));
	}
	
	public List<AccessData> findByDateIPNum(String date1, String date2) {
		return this.find("from AccessData where accessdate between :p1 and :p2", new Parameter(date1, date2));
	}
	public List<?> findByDateIPMonthNum(String date1, String date2,String showType) {
			Session session = this.getSession();
			Query query = null;
			if("center".equals(showType)){
				query=session
						.createSQLQuery("select accessdate,cenpvnum,cenipnum from STAT_ACCESSDATEDATA where funitemId is null and org is null and length(accessdate) = 6 and accessdate between ? and ? order by accessDate ");

			}else if("province".equals(showType)){
				query=session
						.createSQLQuery("select accessdate,propvnum,proipnum from STAT_ACCESSDATEDATA where funitemId is null and org is null and length(accessdate) = 6 and accessdate between ? and ? order by accessDate ");
			}else{
				query=session
						.createSQLQuery("select accessdate,pvnum,ipnum from STAT_ACCESSDATEDATA where funitemId is null and org is null and length(accessdate) = 6 and accessdate between ? and ? order by accessDate");
				
			}
			query.setString(0, date1);
			query.setString(1, date2);
			List<?> list = query.list();
			return list;
		}
	public List<?> findByDateIPMonthNum(String date1, String date2,String showType,String funitemId) {
		Session session = this.getSession();
		Query query = null;
		if("center".equals(showType)){
			query=session
					.createSQLQuery("select accessdate,cenpvnum,cenipnum from STAT_ACCESSDATEDATA where funitemId=? and length(accessdate) = 6 and accessdate between ? and ? order by accessDate ");
		}else if("province".equals(showType)){
			query=session
					.createSQLQuery("select accessdate,propvnum,proipnum from STAT_ACCESSDATEDATA where funitemId=?  and length(accessdate) = 6 and accessdate between ? and ? order by accessDate ");

		}else{
			query=session
					.createSQLQuery("select accessdate,pvnum,ipnum from STAT_ACCESSDATEDATA where funitemId=? and length(accessdate) = 6 and accessdate between ? and ? order by accessDate");

		}
		query.setInteger(0, Integer.parseInt(funitemId));
		query.setString(1, date1);
		query.setString(2, date2);
		List<?> list = query.list();
		return list;
	}
	@Transactional
	public void saveYesterdayIpNum(String accessdate,int ipnum,int yesterdayIPNumCenter,int yesterdayIPNumProvince,int pvNum,int pvNumcenter,int pvNumProvince){
		AccessData accessData=new AccessData();
		accessData.setAccessdate(accessdate);
		accessData.setIpnum(ipnum);
		accessData.setProIpNum(yesterdayIPNumProvince);
		accessData.setCenIpNum(yesterdayIPNumCenter);
		accessData.setPvnum(pvNum);
		accessData.setCenPvNum(pvNumcenter);
		accessData.setPropvNum(pvNumProvince);
		this.save(accessData);
	}
	@Transactional
	public void updateYesterdayIpNum(String accessdate,int ipnum,int yesterdayIPNumCenter,int yesterdayIPNumProvince,int pvNum){

		String sql="update AccessData set ipnum=:p1,pvnum=:p2,proIpNum=:p3,cenIpNum=:p4 where accessdate=:p5";
		this.update(sql, new Parameter(ipnum,pvNum,yesterdayIPNumProvince,yesterdayIPNumCenter,accessdate));
	}
	@Transactional
	public void deleteYesterdayIpNum(String accessdate){
		String sql="delete from AccessData where accessdate=:p1 and pvnum is null";
		this.update(sql, new Parameter(accessdate));
	}
	/**
	 * 
	 * 查找昨天是否有数据
	 * @param date
	 * @return
	 */
	@Transactional
	public List<AccessData> findYesterdayAccessData(String date){
		return this.find("from AccessData where accessdate =:p1 and pvnum is not null ",new Parameter(date) );
	}
}
