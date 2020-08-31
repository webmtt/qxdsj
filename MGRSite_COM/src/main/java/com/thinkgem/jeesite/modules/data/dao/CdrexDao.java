
package com.thinkgem.jeesite.modules.data.dao;


import com.thinkgem.jeesite.common.persistence.BaseDao_rdb;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.data.entity.Cdrex;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用户DAO接口
 */
@Repository
public class CdrexDao extends BaseDao_rdb<Cdrex> {
	public Map<String, Integer> getCountOfCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		Session session = this.getSession();
		Query query = null;
		String hql="";
		// and dstId!='' and duration!=0
		if(groupType!=null && !"".equals(groupType)){
			if("%Y%m%d".equals(type)){
				hql = "select dstId,sum(1) from cdr_ex  where date_format(callDate,'%Y%m%d')>= ? and date_format(callDate,'%Y%m%d')<= ? and dstId is not null and dstId !='' and disposition='ANSWERED' and duration!=0 GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear);
				query.setString(1, endYear);
			}else{
				hql = "select dstId,sum(1) from cdr_ex  where date_format(callDate,'%Y%m')>= ? and date_format(callDate,'%Y%m')<= ? and dstId is not null and dstId !='' and disposition='ANSWERED' and duration!=0 GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear+startMonth);
				query.setString(1, endYear+endMonth);
			}
		}else{
			if("%Y%m".equals(type)){
				hql = "select date_format(callDate,?),sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<= ? and dstId is not null and dstId !='' and disposition='ANSWERED' and duration!=0 GROUP BY date_format(callDate,?) ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, type);
				query.setString(3, startYear + startMonth);
				query.setString(4, endYear + endMonth);
				query.setString(5, type);
			}else if("%Y".equals(type)){
				hql = "select date_format(callDate,?),sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !='' and disposition='ANSWERED' and duration!=0 GROUP BY date_format(callDate,?) ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, startYear);
				query.setString(3, type);
				query.setString(4, endYear);
				query.setString(5, type);
			}else if("%Y%m%d".equals(type)){
				hql = "select 1,sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !='' and disposition='ANSWERED' and duration!=0 ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				  query.setString(0,type);
				  query.setString(1, startYear);
				  query.setString(2,type);
				  query.setString(3,endYear);
			}
		}
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		List list = query.list();
		if(list.size()==0){
			return map;
		}
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			String a=0+"";
			if(objs[1]!=null){
				a=objs[1].toString();
			}
			map.put(objs[0].toString(), Integer.valueOf(a));
		}
		return map;
	}
	public Map<String, Integer> getCountOfMissCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		Session session = this.getSession();
		Query query = null;
		String hql="";
		if(groupType!=null && !"".equals(groupType)){
			if("%Y%m%d".equals(type)){
				hql = "select dstId,sum(1) from cdr_ex  where date_format(callDate,'%Y%m%d')>= ? and date_format(callDate,'%Y%m%d')<= ? and duration!=0 and dstId is not null and dstId !='' and disposition='NO ANSWER' and billsec=0 GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear);
				query.setString(1, endYear);
			}else{
				hql = "select dstId,sum(1) from cdr_ex  where date_format(callDate,'%Y%m')>= ? and date_format(callDate,'%Y%m')<= ? and duration!=0 and dstId is not null and dstId !='' and disposition='NO ANSWER' and billsec=0 GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear+startMonth);
				query.setString(1, endYear+endMonth);
			}
		}else{
			if("%Y%m".equals(type)){
				hql = "select date_format(callDate,?),sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<= ? and duration!=0 and dstId is not null and dstId !='' and disposition='NO ANSWER' and billsec=0 GROUP BY date_format(callDate,?) ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, type);
				query.setString(3, startYear + startMonth);
				query.setString(4, endYear + endMonth);
				query.setString(5, type);
			}else if("%Y".equals(type)){
				hql = "select date_format(callDate,?),sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !='' and disposition='NO ANSWER' and billsec=0 GROUP BY date_format(callDate,?) ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, startYear);
				query.setString(3, type);
				query.setString(4, endYear);
				query.setString(5, type);
			}else if("%Y%m%d".equals(type)){
				hql = "select 1,sum(1) from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !='' and disposition='NO ANSWER' and billsec=0 ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				  query.setString(0,type);
				  query.setString(1, startYear);
				  query.setString(2,type);
				  query.setString(3,endYear);
			}
		}
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		List list = query.list();
		if(list.size()==0){
			return map;
		}
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			String a=0+"";
			if(objs[1]!=null){
				a=objs[1].toString();
			}
			map.put(objs[0].toString(), Integer.valueOf(a));
		}
		return map;
	}
	public Map<String, Float> getCountReceiveCall(String startYear, String endYear, String startMonth, String endMonth, String groupType, String type) {
		Session session = this.getSession();
		Query query = null;
		String hql="";
		if(groupType!=null && !"".equals(groupType)){
			if("%Y%m%d".equals(type)){
				hql = "select dstId,sum(billSec)/60 from cdr_ex  where date_format(callDate,'%Y%m%d')>= ? and date_format(callDate,'%Y%m%d')<= ? and dstId is not null and dstId !='' GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear);
				query.setString(1, endYear);
			}else{
				hql = "select dstId,sum(billSec)/60 from cdr_ex  where date_format(callDate,'%Y%m')>= ? and date_format(callDate,'%Y%m')<= ? and dstId is not null and dstId !='' GROUP BY dstId ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, startYear+startMonth);
				query.setString(1, endYear+endMonth);
			}
		}else{
			if("%Y%m".equals(type)){
				hql = "select date_format(callDate,?),sum(billSec)/60 from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<= ? and dstId is not null and dstId !='' GROUP BY date_format(callDate,?) ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, type);
				query.setString(3, startYear + startMonth);
				query.setString(4, endYear + endMonth);
				query.setString(5, type);
			}else if("%Y".equals(type)){
				hql = "select date_format(callDate,?),sum(billSec)/60 from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !='' GROUP BY date_format(callDate,?)  ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				query.setString(0, type);
				query.setString(1, type);
				query.setString(2, startYear);
				query.setString(3, type);
				query.setString(4, endYear);
				query.setString(5, type);
			}else if("%Y%m%d".equals(type)){
				hql = "select 1,sum(billSec)/60 from cdr_ex  where date_format(callDate,?)>=? and date_format(callDate,?)<=? and dstId is not null and dstId !=''  ORDER BY callDate DESC";
				query = session.createSQLQuery(hql);
				  query.setString(0,type);
				  query.setString(1, startYear);
				  query.setString(2,type);
				  query.setString(3,endYear);
			}
		}
		Map<String, Float> map = new LinkedHashMap<String, Float>();
		List list = query.list();
		if(list.size()==0){
			return map;
		}
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			String a=0+"";
			if(objs[1]!=null){
				a=objs[1].toString();
			}
			map.put(objs[0].toString(), Float.valueOf(a));
		}
		return map;
	}

	public int getphoneNum(String dateStr) {
		int phoneNum=0;
		String sql="SELECT count(*) FROM cdr_ex WHERE date_format(callDate, '%Y%m%d') = ? AND dstId IS NOT NULL AND dstId != '' AND disposition = 'ANSWERED'";
		Query query =this.getSession().createSQLQuery(sql);
		query.setString(0,dateStr);
		List list=query.list();
		if(list.size()>0){		  
		   phoneNum=Integer.valueOf( list.get(0).toString());
		}
		return phoneNum;
	}
	public int getphoneNumByRange(String startTime,String endTime) {
		int phoneNum=0;
		String sql="SELECT count(*) FROM cdr_ex WHERE callDate >= ? and callDate<=? AND dstId IS NOT NULL AND dstId != '' AND disposition = 'ANSWERED'";
		Query query =this.getSession().createSQLQuery(sql);
		query.setString(0,startTime);
		query.setString(1,endTime);
		List list=query.list();
		if(list.size()>0){		  
		   phoneNum=Integer.valueOf( list.get(0).toString());
		}
		return phoneNum;
	}
	public Page<Cdrex> getByPage(Page<Cdrex> page) {
		Page<Cdrex> p=new Page<Cdrex>();
		String sql="SELECT count(*) FROM cdr_ex where duration!=0 order by callDate";
		Query query =this.getSession().createSQLQuery(sql);
		
		   //StringBuffer sb = new StringBuffer("from Cdrex");
			//sb.append(" order by columnItemID desc");
		
			p=this.find(page,sql);
			return p;
		}
	
	public <Cdrex> Page<Cdrex> find(Page<Cdrex> page, String qlString){
    	return find(page, qlString, null);
    }
	private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
	
	 private String removeOrders(String qlString) {  
	        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
	        Matcher m = p.matcher(qlString);  
	        StringBuffer sb = new StringBuffer();  
	        while (m.find()) {  
	            m.appendReplacement(sb, "");  
	        }
	        m.appendTail(sb);
	        return sb.toString();  
	    }
	 
	 private void setParameter(Query query, Parameter parameter){
			if (parameter != null) {
	            Set<String> keySet = parameter.keySet();
	            for (String string : keySet) {
	                Object value = parameter.get(string);
	                //这里考虑传入的参数是什么类型，不同类型使用的方法不同  
	                if(value instanceof Collection<?>){
	                    query.setParameterList(string, (Collection<?>)value);
	                }else if(value instanceof Object[]){
	                    query.setParameterList(string, (Object[])value);
	                }else{
	                    query.setParameter(string, value);
	                }
	            }
	        }
		}
	 
	 
	 
	 public Query createQuery(String qlString, Parameter parameter){
			Query query = getSession().createSQLQuery(qlString);
			setParameter(query, parameter);
			return query;
		}
	 
	 
	public <Cdrex> Page<Cdrex> find(Page<Cdrex> page, String qlString, Parameter parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
//	        page.setCount(Long.valueOf(createQuery(countQlString, parameter).uniqueResult().toString()));
	       Query query = createQuery(countQlString, parameter);
	       //  Query query =createQuery(countQlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.list());
		return page;
    }
	
	
	
	
	public List<Cdrex> find(String begin) {
		String sql="SELECT * FROM cdr_ex where duration!=0 order by callDate desc  limit ?,20";
		Query query =this.getSession().createSQLQuery(sql);
		int beg=Integer.parseInt(begin);
		String bb=(beg-1)*20+"";
		query.setString(0,bb);
		List list=query.list();
		List<Object> objList = new ArrayList<Object>();
		List<Cdrex> lists = new ArrayList<Cdrex>();
		
		for(Object ob:list){
			objList.add(ob);
		}
		Object [] obj=null;
		
		for (int j = 0; j<objList.size();j++) {
		    obj=(Object[])objList.get(j);
			String ID= obj[0].toString();
			String callDate=obj[1].toString();
			String dstId=obj[6].toString();
			String disPosition=obj[15].toString(); 
			String billSec=obj[14].toString(); 
			int s=Integer.parseInt(billSec);
			int s1=s%60;
			int m=s/60;
			int m1=m%60;
			int h=m/60;
			String bill=(h>10?(h+""):("0"+h))+":"+(m1>10?(m1+""):("0"+m1))+":"+(s1>10?(s1+""):("0"+s1))+"("+billSec+")";
			String src=obj[3].toString();
			String srcid=obj[4].toString();
			String dst=obj[5].toString();
			String clid=obj[2].toString();
			String duration=obj[13].toString();
			int d=Integer.parseInt(duration);
			int a=d-s;
			int s2=a%60;
			int m2=a/60;
			int m3=m2%60;
			int h2=m2/60;
			String dura=(h2>10?(h2+""):("0"+h2))+":"+(m3>10?(m3+""):("0"+m3))+":"+(s2>10?(s2+""):("0"+s2))+"("+a+")";
			Cdrex cdr=new Cdrex();
			cdr.setDstId(dstId);
			cdr.setDisPosition(disPosition);
			cdr.setCallDate(callDate);
			cdr.setClid(clid);
			cdr.setDst(dst);
			cdr.setDstId(dstId);
			cdr.setDuration(dura);
			cdr.setID(Double.parseDouble(ID));
			cdr.setSrc(src);
			cdr.setSrcid(srcid);
			cdr.setBillSec(bill);
			lists.add(cdr);
			
		}	
		
		return lists;
	}
	/**
	 * 呼入和呼出次数
	 * @param startTime
	 * @param endTime
	 * @param type  0呼入  1接听  2呼出 3接通次数
	 * @return
	 */
	public Map<String, Integer> getIncomingCall(String startTime, String endTime,String type) {
		String hql="";
		if("0".equals(type)){
			hql="select dstId,sum(1) from cdr_ex where calldate >=:p1 and calldate <=:p2 and dstId!='' and duration!=0 group by dstId";
		}else if("1".equals(type)){
			hql="select dstId,sum(1) from cdr_ex where calldate >= :p1 and calldate <=:p2 and dstId!='' and duration!=0  and billsec!=0 and disposition='ANSWERED' group by dstId;";
		}else if("2".equals(type)){
			hql="select srcid,sum(1) from cdr_ex where calldate >= :p1 and calldate <= :p2 and srcid!='' group by srcid;";
		}else if("3".equals(type)){
			hql="select srcid,sum(1) from cdr_ex where calldate >= :p1 and calldate <= :p2 and srcid!='' and duration!=0 and billsec!=0 and disposition='ANSWERED' group by srcid;";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		if(list.size()==0){
			return map;
		}else{
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				String a="0";
				if(objs[1]!=null){
					a=objs[1].toString();
				}
				map.put(objs[0].toString(),  stringToInt(a));
			}
			return map;
		}
	}
	/**
	 * 呼入和呼出次数
	 * @param startTime
	 * @param endTime
	 * @param type  0振铃时长  1接听时长  2接听总时长  3 平均呼出时长
	 * @return
	 */
	public Map<String, Integer> getIncomingCall2(String startTime, String endTime,String type) {
		String hql="";
		if("0".equals(type)){
			hql="select dstId,sum(duration-billsec)/count(*) from cdr_ex where calldate >= :p1 and calldate <= :p2 and dstId!='' and duration!=0 and disposition='ANSWERED' GROUP BY dstId";
		}else if("1".equals(type)){
			hql="select dstId,sum(billsec)/count(*) from cdr_ex where calldate >= :p1 and calldate <= :p2 and dstId!='' and duration!=0 and disposition='ANSWERED' GROUP BY dstId";
		}else if("2".equals(type)){
			hql="select srcid,sum(billsec)/count(*) from cdr_ex where calldate >= :p1 and calldate <= :p2 and srcid!=''  GROUP BY srcid";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		if(list.size()==0){
			return map;
		}else{
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				String a="0";
				if(objs[1]!=null){
					a=objs[1].toString();
				}
				
				map.put(objs[0].toString(), stringToInt(a));
			}
			return map;
		}
	}
	public int stringToInt(String string){
		  double temp=Double.valueOf(string);
		  return (int)temp;
    }
	/**
	 * 接听总时长和呼出总时长 保留两位
	 * @param startTime
	 * @param endTime
	 * @param type  0接听总时长  1呼出总时长
	 * @return
	 */
	public Map<String, Float> getIncomingCall3(String startTime, String endTime,String type) {
		String hql="";
		if("0".equals(type)){
			hql="select  dstId,sum(billsec)/3600 from cdr_ex where  calldate >= :p1 and calldate <= :p2 and dstId!='' and duration!=0 and disposition='ANSWERED' GROUP BY dstId";
		}else if("1".equals(type)){
			hql="select srcid,sum(duration)/3600 from cdr_ex where calldate >= :p1 and calldate <= :p2 and srcid!='' GROUP BY srcid";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		Map<String, Float> map = new LinkedHashMap<String, Float>();
		if(list.size()==0){
			return map;
		}else{
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				String a="0";
				if(objs[1]!=null){
					a=objs[1].toString();
				}
				Float result=Float.valueOf(a);
				DecimalFormat   fnum  =   new  DecimalFormat("##0.00");
				Float   dd=Float.valueOf(fnum.format(result)); 
				map.put(objs[0].toString(), dd);
			}
			return map;
		}
	}
	public List<Object[]> getList(String startTime, String endTime) {
		String sql="select  dstId from cdr_ex where calldate >=:p1 and calldate <=:p2  and dstId!=''  GROUP BY dstId order by dstId";
		List<Object[]> list=this.findBySql(sql,new Parameter(startTime,endTime));
		return list;
		
	}
	/**
	 * 时间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> getAllTime(String startTime, String endTime,String type) {
		String 	hql="";
		if("day".equals(type)){
			hql="select date_format(callDate,'%Y-%m-%d'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m-%d') >=:p1 and date_format(callDate,'%Y-%m-%d') <=:p2  and  (dstId!='' or  srcid!='')  group by date_format(callDate,'%Y-%m-%d') order by date_format(callDate,'%Y-%m-%d')";
		}else if("month".equals(type)){
			hql="select date_format(callDate,'%Y-%m'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m') >=:p1 and date_format(callDate,'%Y-%m') <=:p2  and  (dstId!='' or  srcid!='')  group by date_format(callDate,'%Y-%m') order by date_format(callDate,'%Y-%m')";
		}else if("year".equals(type)){
			hql="select date_format(callDate,'%Y'),sum(1) from cdr_ex where date_format(callDate,'%Y') >=:p1 and date_format(callDate,'%Y') <=:p2  and  (dstId!='' or  srcid!='')  group by date_format(callDate,'%Y') order by date_format(callDate,'%Y')";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		return list;
	}
	/**
	 * 根据开始时间和结束时间查询接通
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> getReceive(String startTime, String endTime,String type) {
		String 	hql="";
		if("day".equals(type)){
			hql="select date_format(callDate,'%Y-%m-%d'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m-%d') >=:p1 and date_format(callDate,'%Y-%m-%d') <=:p2  and  dstId!='' and disposition='ANSWERED' and duration!=0 and billsec!=0 group by date_format(callDate,'%Y-%m-%d') order by date_format(callDate,'%Y-%m-%d')";	
		}else if("month".equals(type)){
			hql="select date_format(callDate,'%Y-%m'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m') >=:p1 and date_format(callDate,'%Y-%m') <=:p2  and  dstId!='' and disposition='ANSWERED' and duration!=0 and billsec!=0 group by date_format(callDate,'%Y-%m') order by date_format(callDate,'%Y-%m')";
		}else if("year".equals(type)){
			hql="select date_format(callDate,'%Y'),sum(1) from cdr_ex where date_format(callDate,'%Y') >=:p1 and date_format(callDate,'%Y') <=:p2  and  dstId!='' and disposition='ANSWERED' and duration!=0 and billsec!=0 group by date_format(callDate,'%Y') order by date_format(callDate,'%Y')";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		return list;
	}
	/**
	 * 呼损
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> getCallLoss(String startTime, String endTime,String type) {
		String 	hql="";
		if("day".equals(type)){
			hql="select date_format(callDate,'%Y-%m-%d'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m-%d') >=:p1 and date_format(callDate,'%Y-%m-%d') <=:p2 and disposition='ANSWERED' and  dstId!='' and duration=0 group by date_format(callDate,'%Y-%m-%d') order by date_format(callDate,'%Y-%m-%d')";
		}else if("month".equals(type)){
			hql="select date_format(callDate,'%Y-%m'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m') >=:p1 and date_format(callDate,'%Y-%m') <=:p2 and disposition='ANSWERED' and  dstId!='' and duration=0 group by date_format(callDate,'%Y-%m') order by date_format(callDate,'%Y-%m')";
		}else if("year".equals(type)){
			hql="select date_format(callDate,'%Y'),sum(1) from cdr_ex where date_format(callDate,'%Y') >=:p1 and date_format(callDate,'%Y') <=:p2 and disposition='ANSWERED' and  dstId!='' and duration=0 group by date_format(callDate,'%Y') order by date_format(callDate,'%Y')";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		return list;
	}
	/**
	 * 未接通
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> getNoReceive(String startTime, String endTime,String type) {
		String 	hql="";
		if("day".equals(type)){
		 	hql="select date_format(callDate,'%Y-%m-%d'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m-%d') >=:p1 and date_format(callDate,'%Y-%m-%d') <=:p2 and disposition='NO ANSWER' and duration!=0 and billsec=0 and  dstId!='' group by date_format(callDate,'%Y-%m-%d') order by date_format(callDate,'%Y-%m-%d')";
		}else if("month".equals(type)){
		 	hql="select date_format(callDate,'%Y-%m'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m') >=:p1 and date_format(callDate,'%Y-%m') <=:p2 and disposition='NO ANSWER' and duration!=0 and billsec=0 and  dstId!='' group by date_format(callDate,'%Y-%m') order by date_format(callDate,'%Y-%m')";
		}else if("year".equals(type)){
		 	hql="select date_format(callDate,'%Y'),sum(1) from cdr_ex where date_format(callDate,'%Y') >=:p1 and date_format(callDate,'%Y') <=:p2 and disposition='NO ANSWER' and duration!=0 and billsec=0 and  dstId!='' group by date_format(callDate,'%Y') order by date_format(callDate,'%Y')";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		return list;
	}
	/**
	 * 呼出
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> getExhale(String startTime, String endTime,String type) {
		String 	hql="";
		if("day".equals(type)){
		 	hql="select date_format(callDate,'%Y-%m-%d'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m-%d') >=:p1 and date_format(callDate,'%Y-%m-%d') <=:p2  and  srcid!='' and duration!=0 group by date_format(callDate,'%Y-%m-%d') order by date_format(callDate,'%Y-%m-%d')";
		}else if("month".equals(type)){
		 	hql="select date_format(callDate,'%Y-%m'),sum(1) from cdr_ex where date_format(callDate,'%Y-%m') >=:p1 and date_format(callDate,'%Y-%m') <=:p2  and  srcid!='' and duration!=0 group by date_format(callDate,'%Y-%m') order by date_format(callDate,'%Y-%m')";
		}else if("year".equals(type)){
		 	hql="select date_format(callDate,'%Y'),sum(1) from cdr_ex where date_format(callDate,'%Y') >=:p1 and date_format(callDate,'%Y') <=:p2  and  srcid!='' and duration!=0 group by date_format(callDate,'%Y') order by date_format(callDate,'%Y')";
		}
		List list=this.findBySql(hql,new Parameter(startTime,endTime));
		return list;
	}
}
	
