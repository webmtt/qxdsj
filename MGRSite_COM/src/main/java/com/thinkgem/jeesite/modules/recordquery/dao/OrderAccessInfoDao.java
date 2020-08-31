
package com.thinkgem.jeesite.modules.recordquery.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;








import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlData;
import com.thinkgem.jeesite.modules.recordquery.entity.OrderAccessInfo;
import com.thinkgem.jeesite.modules.recordquery.entity.OrderAccessInfoModel;


/**
 * 用户DAO接口
 */
@Repository
public class OrderAccessInfoDao extends BaseDao<OrderAccessInfo> {
	/**
	 * 按时间-分类获取访问量
	 * @return
	 */
	public List<OrderAccessInfoModel> getSumClassByTime(String start_time,String end_time) {
		String hql="";		
		hql = "select * from(select dataCode,SUM(orderNumber) from stat_OrderAccessInfo where  "
				+ "d_datetime >=:p1 AND d_datetime <=:p2 GROUP BY dataCode ORDER BY SUM(orderNumber) desc)"
				+ " where rownum<11";
		List list=this.findBySql(hql,new Parameter(start_time,end_time));
		List result = new ArrayList<OrderAccessInfoModel>();
		for(int i = 0; i < list.size(); ++i){
			Object[] objs = (Object[]) list.get(i);
			OrderAccessInfoModel stat = new OrderAccessInfoModel((String)objs[0],Integer.parseInt(objs[1].toString()));
			result.add(stat);
		}		
		return result;
	}
	/**
	 * 按时间获取总订单量
	 * @return
	 */
	public BigDecimal getTotalByTime(String start_time,String end_time) {
		String hql="";	
		hql = "SELECT SUM(orderNumber) FROM OrderAccessInfo WHERE d_datetime >= ? AND d_datetime <= ?";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		return query.uniqueResult()==null?new BigDecimal(0):new BigDecimal(query.uniqueResult().toString());
	}
	/**
	 * 按时间-分类获取下载量
	 * @return
	 */
	public List<OrderAccessInfoModel> getDownLoadSumClassByTime(String start_time,String end_time) {
		String hql="";		
		hql = "select * from(select dataCode,ROUND(SUM(filequantity)/1024/1024,2) from "
				+ "stat_OrderAccessInfo where  d_datetime >=:p1 AND d_datetime <=:p2 "
				+ "GROUP BY dataCode ORDER BY SUM(filequantity) desc) where rownum<11";
		List list=this.findBySql(hql,new Parameter(start_time,end_time));
		List result = new ArrayList<OrderAccessInfoModel>();
		for(int i = 0; i < list.size(); ++i){
			Object[] objs = (Object[]) list.get(i);
			OrderAccessInfoModel stat = new OrderAccessInfoModel((String)objs[0],Double.valueOf(objs[1].toString()));
			result.add(stat);
		}		
		return result;
	}
	/**
	 * 按总量   获取总下载量
	 * @return
	 */
	public List<OrderAccessInfo> getDownLoadSumByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		List<OrderAccessInfo> list1=new ArrayList<OrderAccessInfo>();
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("4")) {
			hql="SELECT substr(f.d_datetime,0,4),sum(f.ordernumber) FROM stat_orderaccessinfo f WHERE substr(f.d_datetime,0,4) >= ? AND substr(f.d_datetime,0,4) <= ? AND f.dataCode is not null GROUP BY substr(f.d_datetime,0,4) ORDER BY CONVERT(substr(f.d_datetime,0,4),SIGNED) "+orderType;
		}else if(timeType.equals("6")){
			hql="SELECT substr(f.d_datetime,0,6),sum(f.ordernumber) FROM stat_orderaccessinfo f WHERE substr(f.d_datetime,0,6) >= ? AND substr(f.d_datetime,0,6) <= ? AND f.dataCode is not null GROUP BY substr(f.d_datetime,0,6) ORDER BY CONVERT(substr(f.d_datetime,0,6),SIGNED) "+orderType;
		}else if(timeType.equals("8")){
			hql="SELECT substr(f.d_datetime,0,8),sum(f.ordernumber) FROM stat_orderaccessinfo f WHERE substr(f.d_datetime,0,8) >= ? AND substr(f.d_datetime,0,8) <= ? AND f.dataCode is not null GROUP BY substr(f.d_datetime,0,8) ORDER BY CONVERT(substr(f.d_datetime,0,8),SIGNED) "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			OrderAccessInfo f=new OrderAccessInfo();
			f.setD_datetime(obj[0].toString());
			f.setOrderNumber(Integer.valueOf(obj[1].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	public List getDownLoadSumClassByTime(String start_time, String end_time, String timeType, Integer page,
			                    Integer row, String orderType,String org) {
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		
		String hql="SELECT  a.dataCode,b.chnName,sum(a.ordernumber) "
				  + " FROM stat_orderaccessinfo a left join " +
				  "(Select datacode,chnname As chnname From BMD_DATADEF Union All Select datacode,datachnname As chnname From BMD_DATASEARCHDEF Where parentdatacode Is Not Null) " +
				  "b on a.dataCode =b.dataCode ";
		if(timeType.equals("4")) {
			hql+=" WHERE substr(a.d_datetime,0,4) >= ? AND substr(a.d_datetime,0,4)<= ? ";
		}else if(timeType.equals("6")){
			hql+=" WHERE substr(a.d_datetime,0,6) >= ? AND substr(a.d_datetime,0,6)<= ? ";
		}else if(timeType.equals("8")){
			hql+=" WHERE substr(a.d_datetime,0,8) >= ? AND substr(a.d_datetime,0,8)<= ? ";
		}
		if(org!=null&&!"".equals(org)&&!"0".equals(org)){
			hql+="and substr(a.orgin,0,6)=?";
		}
		hql+=" AND a.dataCode is not null "
			+ " GROUP BY a.dataCode, b.chnName "
			+ " ORDER BY sum(a.ordernumber)"+ orderType;
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		if(org!=null&&!"".equals(org)&&!"0".equals(org)){
			query.setString(2, org);
		}
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		
		List<Object[]> list=query.list();
		
		List list1=new ArrayList();
		List dataCodeList=new ArrayList();
		List chnNameList=new ArrayList();
		List downSizeList=new ArrayList();
//		List accessDateList=new ArrayList();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			String dataCode=obj[0].toString();
			String chnName="";
			if(obj[1]==null){
				 chnName=obj[0].toString();
			}else{
				 chnName=obj[1].toString();
			}
			long downSize=Long.valueOf(obj[2].toString());
			dataCodeList.add(dataCode);
			chnNameList.add(chnName);
			downSizeList.add(downSize);
		}
		list1.add(dataCodeList);
		list1.add(chnNameList);
		list1.add(downSizeList);
		return list1;
	}
	/**
	 * 查询单位和省份
	 * @param type 0表示省份  1表示国家局
	 * @return
	 */
	public List<Object[]> getDepart(String type) {
		String hql="";
		if("0".equals(type)){
			 hql="Select Id,Name From Sup_Orginfo a  Where parentid='001' And a.Name  Like '%气象局' And  a.Name!='中国气象局'";
		}else{
			 hql="select substr(a.id,0,6),Name From Sup_Orginfo a  Where parentid='001' And a.Name not  Like '%气象局' or  a.Name='中国气象局'";
		}
		return this.findBySql(hql);
	}
}
