package com.thinkgem.jeesite.modules.statistics.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.statistics.entity.AccessDateData;

@Repository
public class AccessDateDataDao extends BaseDao<AccessDateData> {
	public List<AccessDateData> findByDate(String date) {
		return this.find("from test.AccessDateData where accessdate=:p1", new Parameter(date));
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
	public List<String> statYears(){
		String sql="Select substr(accessdate,0,4) From test.STAT_ACCESSDATEIP Group By substr(accessdate,0,4)";
		List list=this.findBySql(sql);
		return list;
		
	}
	public List<String> getOrg(String org){
		String sql="Select proname From PORTAL_IPINFOR Where invalid=0 and sourcetype=:p1 Group By proname";
		List list=this.findBySql(sql,new Parameter(org));
		return list;
		
	}
	public List<String> getname(String org){
		String sql="Select proname From PORTAL_IPINFOR Where enname=:p1";
		List list=this.findBySql(sql,new Parameter(org));
		return list;
		
	}
	public List<String[]> getOrgAll(){
		String sql="Select proname,enname,sourcetype From PORTAL_IPINFOR Where invalid=0  Group By proname,enname,sourcetype Order By sourcetype";
		List list=this.findBySql(sql);
		return list;
		
	}
	public List getOrgId(String org){
		String sql="Select enname,proname From PORTAL_IPINFOR Where invalid=0 and  sourcetype=:p1 Group By enname,proname";
		List list=this.findBySql(sql,new Parameter(org));
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
	public List<Object[]> getSumByTime(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType,String sourceType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("6")){
			if("0".equals(sourceType)){
				hql = "Select accessdate,ipnum,pvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and org Is Null And substr(accessdate,0,6)>=? And substr(accessdate,0,6)<=? And length(accessdate)=6 order by accessdate  "+orderType;
			}else if("province".equals(sourceType)){
				hql = "Select accessdate,proipnum,propvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and org Is Null And substr(accessdate,0,6)>=? And substr(accessdate,0,6)<=? And length(accessdate)=6 order by accessdate  "+orderType;
			}else if("center".equals(sourceType)){
				hql = "Select accessdate,cenipnum,cenpvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and org Is Null And substr(accessdate,0,6)>=? And substr(accessdate,0,6)<=? And length(accessdate)=6 order by accessdate  "+orderType;
			}
		}else if(timeType.equals("8")) {
			if("0".equals(sourceType)){
				hql = "Select accessdate,ipnum,pvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and  org Is Null And accessdate>=? And accessdate<=? And length(accessdate)=8 order by accessdate "+orderType;
			}else if("province".equals(sourceType)){
				hql = "Select accessdate,proipnum,propvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and  org Is Null And accessdate>=? And accessdate<=? And length(accessdate)=8 order by accessdate "+orderType;
			}else if("center".equals(sourceType)){
				hql = "Select accessdate,cenipnum,cenpvnum From STAT_ACCESSDATEDATA Where funitemid Is Null and  org Is Null And accessdate>=? And accessdate<=? And length(accessdate)=8 order by accessdate "+orderType;
			}
		}else if(timeType.equals("1")) {
			if("0".equals(sourceType)){
				hql = "Select accessdate||accesstime,Sum(ipnum),Sum(pvnum) From STAT_ACCESSIP Where accessdate||accesstime>=? And accessdate||accesstime<=? Group By accessdate||accesstime Order By accessdate||accesstime "+orderType;
			}else if("province".equals(sourceType)){
				hql = "Select accessdate||accesstime,Sum(ipnum),Sum(pvnum) From STAT_ACCESSIP Where accessdate||accesstime>=? And accessdate||accesstime<=? and sourceType='province' Group By accessdate||accesstime Order By accessdate||accesstime "+orderType;
			}else if("center".equals(sourceType)){
				hql = "Select accessdate||accesstime,Sum(ipnum),Sum(pvnum) From STAT_ACCESSIP Where accessdate||accesstime>=? And accessdate||accesstime<=? and sourceType='center' Group By accessdate||accesstime Order By accessdate||accesstime "+orderType;
			}
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
	 * 总量和逐月的数据
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTimeItem(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(timeType.equals("6")){
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATA Where Accessdate >=? And Accessdate <=? And org Is Null  and funitemid=?  and   length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATA Where Accessdate >=? And Accessdate <=? And org Is Null and funitemid=? And  length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=?  Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
			}
		}else if(timeType.equals("8")) {
			//0表示 总的一级栏目
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql="Select Accessdate,Sum(allipnumber),Sum(allpvnumber),round(Sum(allpvnumber*staytime)/Sum(allpvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allpvnumber != '0' And itemid!='0' and allipnumber is not null  And  accessdate>=? And accessdate<=? and  sourcetype Not In('other') and substr(itemid,0,1)=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(allipnumber),Sum(allpvnumber),round(Sum(allpvnumber*staytime)/Sum(allpvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allpvnumber != '0' and allipnumber is not null And  accessdate>=? And accessdate<=?  and  sourcetype Not In('other') and substr(itemid,0,3)=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				//hql = "Select Accessdate,Sum(allipnumber),Sum(pvnumber),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And allpvnumber != '0' and allipnumber is not null And  accessdate>=? And accessdate<=?  and  sourcetype Not In('other') and substr(itemid,0,5)=? Group By Accessdate Order By Accessdate "+orderType;
				hql="select Accessdate,Sum(pvnum),Sum(ipnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) from test.stat_accessdatedatadetail t where Length(funitemid)=5 And pvnum != '0' and pvnum is not null And  accessdate>=? And accessdate<=?  and substr(funitemid,0,5)=? Group By Accessdate Order By Accessdate";
				
			}
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		query.setString(2, funcItemId);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
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
	public List<Object[]> getSumByTimeItemB(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(timeType.equals("6")){
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATA Where Accessdate >=? And Accessdate <=? And org Is Null  and funitemid=?  and   length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATA Where Accessdate >=? And Accessdate <=? And org Is Null and funitemid=? And  length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=?  Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=?  Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
			}
		}else if(timeType.equals("8")) {
			//0表示 总的一级栏目
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql="Select Accessdate,Sum(allipnumber),Sum(allpvnumber),round(Sum(allpvnumber*staytime)/Sum(allpvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allpvnumber != '0' And itemid!='0' and allipnumber is not null  And  accessdate>=? And accessdate<=? and  sourcetype Not In('other') and substr(itemid,0,1)=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(allipnumber),Sum(allpvnumber),round(Sum(allpvnumber*staytime)/Sum(allpvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allpvnumber != '0' and allipnumber is not null And  accessdate>=? And accessdate<=?  and  sourcetype Not In('other') and substr(itemid,0,3)=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				//hql = "Select Accessdate,Sum(allipnumber),Sum(pvnumber),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And allpvnumber != '0' and allipnumber is not null And  accessdate>=? And accessdate<=?  and  sourcetype Not In('other') and substr(itemid,0,5)=? Group By Accessdate Order By Accessdate "+orderType;
				hql="select Accessdate,Sum(pvnum),Sum(ipnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) from test.stat_accessdatedatadetail t where Length(funitemid)=5 And pvnum != '0' and pvnum is not null And  accessdate>=? And accessdate<=?  and substr(funitemid,0,5)=? Group By Accessdate Order By Accessdate";
				
			}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
				//hql = "Select Accessdate,Sum(allipnumber),Sum(pvnumber),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And allpvnumber != '0' and allipnumber is not null And  accessdate>=? And accessdate<=?  and  sourcetype Not In('other') and substr(itemid,0,5)=? Group By Accessdate Order By Accessdate "+orderType;
				hql="select Accessdate,Sum(pvnum),Sum(ipnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) from test.stat_accessdatedatadetail t where Length(funitemid)=7 And pvnum != '0' and pvnum is not null And  accessdate>=? And accessdate<=?  and substr(funitemid,0,7)=? Group By Accessdate Order By Accessdate";
				
			}
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		query.setString(2, funcItemId);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
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
	public List<Object[]> getSumByTimeItem0(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType,String org) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(timeType.equals("6")){
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=?  and   length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=? And  length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=?  Group By Accessdate Order By Substr(Accessdate, 0, 6) "+orderType;
			}
		}else if(timeType.equals("8")) {
			//0表示 总的一级栏目
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql="Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And  length(accessdate)=8 And pvnum != '0' And funitemid!='0' and ipnum is not null  And  accessdate>=? And accessdate<=?  and substr(funitemid,0,1)=? And org=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And  length(accessdate)=8 And pvnum != '0' and ipnum is not null And  accessdate>=? And accessdate<=?   and substr(funitemid,0,3)=?  And org=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And  length(accessdate)=8 And pvnum != '0' and ipnum is not null And  accessdate>=? And accessdate<=?   and substr(funitemid,0,5)=?  And org=? Group By Accessdate Order By Accessdate "+orderType;
			}
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		query.setString(2, funcItemId);
		query.setString(3, org);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
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
	public List<Object[]> getSumByTimeItemA(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType,String org) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(timeType.equals("6")){
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=?  and   length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=? And  length(accessdate)=6 Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=?  Group By Accessdate Order By Substr(Accessdate, 0, 6) "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
				hql = "Select Substr(Accessdate, 0, 6),Sum(ipnum),Sum(pvnum) From test.STAT_ACCESSDATEDATADETAIL Where Accessdate >=? And Accessdate <=?  and funitemid=? And org=?  Group By Accessdate Order By Substr(Accessdate, 0, 6) "+orderType;
			}
		}else if(timeType.equals("8")) {
			//0表示 总的一级栏目
			if(!"".equals(funcItemId)&&funcItemId.length()==1){
				hql="Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And  length(accessdate)=8 And pvnum != '0' And funitemid!='0' and ipnum is not null  And  accessdate>=? And accessdate<=?  and substr(funitemid,0,1)=? And org=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And  length(accessdate)=8 And pvnum != '0' and ipnum is not null And  accessdate>=? And accessdate<=?   and substr(funitemid,0,3)=?  And org=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And  length(accessdate)=8 And pvnum != '0' and ipnum is not null And  accessdate>=? And accessdate<=?   and substr(funitemid,0,5)=?  And org=? Group By Accessdate Order By Accessdate "+orderType;
			}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
				hql = "Select Accessdate,Sum(ipnum),Sum(pvnum),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=7 And  length(accessdate)=8 And pvnum != '0' and ipnum is not null And  accessdate>=? And accessdate<=?   and substr(funitemid,0,7)=?  And org=? Group By Accessdate Order By Accessdate "+orderType;
			}
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		query.setString(2, funcItemId);
		query.setString(3, org);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	/**
	 * 月的平均提留时间
	 * @param start_time
	 * @param end_time
	 * @param funcItemId
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTimeItem2(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(!"".equals(funcItemId)&&funcItemId.length()==1){
			hql="Select Substr(Accessdate, 0, 6),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allPvnumber != '0' And itemid!='0'  And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  sourcetype Not In('other') And Substr(Itemid, 0, 1)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allPvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  and  sourcetype Not In('other') And Substr(Itemid, 0, 3)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?   And Substr(funitemid, 0, 5)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=7 And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?   And Substr(funitemid, 0, 7)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
	    if((timeType.equals("6")&&!"0".equals(funcItemId))){
			query.setString(2, funcItemId);
		}
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
	/**
	 * 月的平均提留时间
	 * @param start_time
	 * @param end_time
	 * @param funcItemId
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<Object[]> getSumByTimeItem22(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
			String orderType,String org) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		//6  按月份
		if(!"".equals(funcItemId)&&funcItemId.length()==1){
			hql="Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And (Length(Accessdate)=6) And pvnum != '0' And funitemid!='0'  And  substr(Accessdate,0,6)>=?  And substr(Accessdate,0,6)<=? and org=?  And Substr(funitemid, 0, 1)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==3){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=3) And (Length(Accessdate)=6) And pvnum != '0'   And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and org=?  And Substr(funitemid, 0, 3)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==5){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=5) And (Length(Accessdate)=6) And pvnum != '0'   And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and org=?  And Substr(funitemid, 0, 5)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}else if(!"".equals(funcItemId)&&funcItemId.length()==7){
			hql = "Select Substr(Accessdate, 0, 6),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=7) And (Length(Accessdate)=8) And pvnum != '0'   And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and org=?  And Substr(funitemid, 0, 7)=? Group By Substr(Accessdate, 0, 6) Order By Substr(Accessdate, 0, 6) "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		query.setString(2, org);
	    if((timeType.equals("6")&&!"0".equals(funcItemId))){
			query.setString(3, funcItemId);
		}
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List list =  query.list();
		return list;
	}
/**
 * 月的平均提留时间
 * @param start_time
 * @param end_time
 * @param funcItemId
 * @param timeType
 * @param page
 * @param row
 * @param orderType
 * @return
 */
public List<Object[]> getSumByTimeItem3(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
		String orderType) {
	String hql="";
	orderType = (orderType==null?"DESC":orderType);
	timeType = (timeType==null?"8":timeType);
	//6  按月份
	if("6".equals(timeType)){
		if("0".equals(funcItemId)){
			hql="Select substr(itemid,0,1),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allPvnumber != '0' And itemid!='0'  And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  sourcetype Not In('other') Group By substr(itemid,0,1) Order By substr(itemid,0,1) ";
		}else if(funcItemId.length()==1){
			hql = "Select substr(itemid,0,3),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allPvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,3) Order By substr(itemid,0,3) ";
		}else if(funcItemId.length()==3){
			hql = "Select substr(itemid,0,5),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And pvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,5) Order By substr(itemid,0,5) ";
		}
	}else{
		if("0".equals(funcItemId)){
			hql="Select substr(itemid,0,1),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allPvnumber != '0' And itemid!='0'  And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  sourcetype Not In('other') Group By substr(itemid,0,1) Order By substr(itemid,0,1) ";
		}else if(funcItemId.length()==1){
			hql = "Select substr(itemid,0,3),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allPvnumber != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,3) Order By substr(itemid,0,3) ";
		}else if(funcItemId.length()==3){
			hql = "Select substr(itemid,0,5),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And pvnumber != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,5) Order By substr(itemid,0,5) ";
		}
	}
	
	Session session = this.getSession();
	Query query = session.createSQLQuery(hql);
	query.setString(0, start_time);
	query.setString(1, end_time);
    if((timeType.equals("8")&&!"0".equals(funcItemId))){
		query.setString(2, funcItemId+"%");
	}
    if((timeType.equals("6")&&!"0".equals(funcItemId))){
		query.setString(2, funcItemId+"%");
	}
	if(page!=null&&row!=null){
		query.setFirstResult((page-1)*row);
		query.setMaxResults(row);
	}
	List list =  query.list();
	return list;
}

/**
 * 时间段内的平均停留时间
 * @param start_time
 * @param end_time
 * @param funcItemId
 * @param timeType
 * @param page
 * @param row
 * @param orderType
 * @return
 */
public List<Object[]> getSumByTimeItem3A(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
		String orderType) {
	String hql="";
	orderType = (orderType==null?"DESC":orderType);
	timeType = (timeType==null?"8":timeType);
	//6  按月份
	if("6".equals(timeType)){
		if("0".equals(funcItemId)){
			hql="Select substr(itemid,0,1),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allPvnumber != '0' And itemid!='0'  And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  sourcetype Not In('other') Group By substr(itemid,0,1) Order By substr(itemid,0,1) ";
		}else if(funcItemId.length()==1){
			hql = "Select substr(itemid,0,3),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allPvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,3) Order By substr(itemid,0,3) ";
		}else if(funcItemId.length()==3){
			hql = "Select substr(itemid,0,5),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And pvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,5) Order By substr(itemid,0,5) ";
		}else if(funcItemId.length()==5){
			hql = "Select substr(itemid,0,7),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=7 And pvnumber != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=?  And itemid in(select t.funcitemid from test.stat_accessfunconfig t where t.parentfuncitemid=?) and  sourcetype Not In('other') Group By substr(itemid,0,7) Order By Sum(pvnumber),substr(itemid,0,7) ";
		}
	}else{
		if("0".equals(funcItemId)){
			hql="Select substr(itemid,0,1),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where (Length(itemid)=1) And allPvnumber != '0' And itemid!='0'  And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  sourcetype Not In('other') Group By substr(itemid,0,1) Order By substr(itemid,0,1) ";
		}else if(funcItemId.length()==1){
			hql = "Select substr(itemid,0,3),round(Sum(allPvnumber*staytime)/Sum(allPvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=3 And allPvnumber != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,3) Order By substr(itemid,0,3) ";
		}else if(funcItemId.length()==3){
			hql = "Select substr(itemid,0,5),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=5 And pvnumber != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=?  And itemid Like ? and  sourcetype Not In('other') Group By substr(itemid,0,5) Order By substr(itemid,0,5) ";
		}else if(funcItemId.length()==5){
			hql = "Select substr(itemid,0,7),round(Sum(pvnumber*staytime)/Sum(pvnumber),1) From test.STAT_ACCESSFUNCDATEINFO Where Length(itemid)=7 And pvnumber != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=?  And itemid in(select t.funcitemid from test.stat_accessfunconfig t where t.parentfuncitemid=?) and  sourcetype Not In('other') Group By substr(itemid,0,7) Order By Sum(pvnumber),substr(itemid,0,7) ";
		}
		
	}
	
	Session session = this.getSession();
	Query query = session.createSQLQuery(hql);
	query.setString(0, start_time);
	query.setString(1, end_time);
    if((timeType.equals("8")&&!"0".equals(funcItemId))){
		query.setString(2, funcItemId);
	}
    if((timeType.equals("6")&&!"0".equals(funcItemId))){
		query.setString(2, funcItemId);
	}
	if(page!=null&&row!=null){
		query.setFirstResult((page-1)*row);
		query.setMaxResults(row);
	}
	List list =  query.list();
	return list;
}
/**
 * 月的平均提留时间
 * @param start_time
 * @param end_time
 * @param funcItemId
 * @param timeType
 * @param page
 * @param row
 * @param orderType
 * @return
 */
public List<Object[]> getSumByTimeItem4(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
		String orderType,String org) {
	String hql="";
	orderType = (orderType==null?"DESC":orderType);
	timeType = (timeType==null?"8":timeType);
	//6  按月份
		if("6".equals(timeType)){
			if("0".equals(funcItemId)){
				hql="Select substr(funitemid,0,1),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And (Length(Accessdate)=6) And pvnum != '0' And funitemid!='0'  And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? Group By substr(funitemid,0,1) Order By substr(funitemid,0,1)";
			}else if(funcItemId.length()==1){
				hql = "Select substr(funitemid,0,3),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And (Length(Accessdate)=6) And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,3) Order By substr(funitemid,0,3)";
			}else if(funcItemId.length()==3){
				hql = "Select substr(funitemid,0,5),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And (Length(Accessdate)=6) And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,5) Order By substr(funitemid,0,5)";
			}
		}else{
			if("0".equals(funcItemId)){
				hql="Select substr(funitemid,0,1),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And (Length(Accessdate)=8) And pvnum != '0' And funitemid!='0'  And  substr(Accessdate,0,8)>=?  And substr(Accessdate,0,8)<=? and  org=? Group By substr(funitemid,0,1) Order By substr(funitemid,0,1) ";
			}else if(funcItemId.length()==1){
				hql = "Select substr(funitemid,0,3),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And (Length(Accessdate)=8) And pvnum != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,3) Order By substr(funitemid,0,3) ";
			}else if(funcItemId.length()==3){
				hql = "Select substr(funitemid,0,5),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And (Length(Accessdate)=8) And pvnum != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,5) Order By substr(funitemid,0,5) ";
			}
		}
	Session session = this.getSession();
	Query query = session.createSQLQuery(hql);
	query.setString(0, start_time);
	query.setString(1, end_time);
	query.setString(2, org);
	 if((timeType.equals("8")&&!"0".equals(funcItemId))){
			query.setString(3, funcItemId+"%");
		}
	    if((timeType.equals("6")&&!"0".equals(funcItemId))){
			query.setString(3, funcItemId+"%");
		}
	if(page!=null&&row!=null){
		query.setFirstResult((page-1)*row);
		query.setMaxResults(row);
	}
	List list =  query.list();
	return list;
}
/**
 * 数据集的停留时间-开始时间-结束时间
 * @param start_time
 * @param end_time
 * @param funcItemId
 * @param timeType
 * @param page
 * @param row
 * @param orderType
 * @param org
 * @return
 */
public List<Object[]> getSumByTimeItem4A(String start_time, String end_time,String funcItemId, String timeType, Integer page, Integer row,
		String orderType,String org) {
	String hql="";
	orderType = (orderType==null?"DESC":orderType);
	timeType = (timeType==null?"8":timeType);
	//6  按月份
		if("6".equals(timeType)){
			if("0".equals(funcItemId)){
				hql="Select substr(funitemid,0,1),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And (Length(Accessdate)=6) And pvnum != '0' And funitemid!='0'  And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? Group By substr(funitemid,0,1) Order By substr(funitemid,0,1)";
			}else if(funcItemId.length()==1){
				hql = "Select substr(funitemid,0,3),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And (Length(Accessdate)=6) And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,3) Order By substr(funitemid,0,3)";
			}else if(funcItemId.length()==3){
				hql = "Select substr(funitemid,0,5),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And (Length(Accessdate)=6) And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,5) Order By substr(funitemid,0,5)";
			}else if(funcItemId.length()==5){
				hql = "Select substr(funitemid,0,7),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=7 And (Length(Accessdate)=6) And pvnum != '0' And  substr(Accessdate,0,6)>=? And substr(Accessdate,0,6)<=? and  org=? And funitemid in (select t.funcitemid from test.stat_accessfunconfig t where t.parentfuncitemid=?)  Group By substr(funitemid,0,5) Order By substr(funitemid,0,5)";
			}
		}else{
			if("0".equals(funcItemId)){
				hql="Select substr(funitemid,0,1),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where (Length(funitemid)=1) And (Length(Accessdate)=8) And pvnum != '0' And funitemid!='0'  And  substr(Accessdate,0,8)>=?  And substr(Accessdate,0,8)<=? and  org=? Group By substr(funitemid,0,1) Order By substr(funitemid,0,1) ";
			}else if(funcItemId.length()==1){
				hql = "Select substr(funitemid,0,3),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=3 And (Length(Accessdate)=8) And pvnum != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,3) Order By substr(funitemid,0,3) ";
			}else if(funcItemId.length()==3){
				hql = "Select substr(funitemid,0,5),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=5 And (Length(Accessdate)=8) And pvnum != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  org=? And funitemid Like ?  Group By substr(funitemid,0,5) Order By substr(funitemid,0,5) ";
			}else if(funcItemId.length()==5){
				hql = "Select substr(funitemid,0,7),round(Sum(pvnum*staytime)/Sum(pvnum),1) From test.STAT_ACCESSDATEDATADETAIL Where Length(funitemid)=7 And (Length(Accessdate)=8) And pvnum != '0' And  substr(Accessdate,0,8)>=? And substr(Accessdate,0,8)<=? and  org=? And funitemid in (select t.funcitemid from test.stat_accessfunconfig t where t.parentfuncitemid=?)  Group By substr(funitemid,0,7) Order By Sum(pvnum),substr(funitemid,0,7) ";
			}
		}
	Session session = this.getSession();
	Query query = session.createSQLQuery(hql);
	query.setString(0, start_time);
	query.setString(1, end_time);
	query.setString(2, org);
	 if((timeType.equals("8")&&!"0".equals(funcItemId))){
			query.setString(3, funcItemId);
		}
	    if((timeType.equals("6")&&!"0".equals(funcItemId))){
			query.setString(3, funcItemId);
		}
	if(page!=null&&row!=null){
		query.setFirstResult((page-1)*row);
		query.setMaxResults(row);
	}
	List list =  query.list();
	return list;
}
}

