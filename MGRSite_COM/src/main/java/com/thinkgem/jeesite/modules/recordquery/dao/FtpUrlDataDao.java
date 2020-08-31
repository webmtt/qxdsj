package com.thinkgem.jeesite.modules.recordquery.dao;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlData;
@Repository
public class FtpUrlDataDao extends BaseDao<FtpUrlData> {
	public Page<FtpUrlData> getDataByPage(Page page,  String begin, String end,String sortds,String sortad) {
		Page<FtpUrlData> npage=new Page<FtpUrlData>();
		String sql="from FtpUrlData where accessDate>=:p1 and accessDate <=:p2 and downSize>0 ";
		if(("dsd".equals(sortds)&&"add".equals(sortad))){
			sql+="order by accessDate desc,downsize desc";
		}else if("dsd".equals(sortds)&&"adz".equals(sortad)){
			sql+="order by accessDate,downsize desc";
		}else if("dsz".equals(sortds)&&"adz".equals(sortad)){
			sql+="order by accessDate,downsize";
		}else if("dsz".equals(sortds)&&"add".equals(sortad)){
			sql+="order by accessDate desc,downsize";
		}else if("dsn".equals(sortds)&&"add".equals(sortad)){
			sql+="order by accessDate desc";
		}else if("dsn".equals(sortds)&&"adz".equals(sortad)){
			sql+="order by accessDate";
		}else if("adn".equals(sortad)&&"dsd".equals(sortds)){
			sql+="order by downsize desc";
		}else if("adn".equals(sortad)&&"dsz".equals(sortds)){
			sql+="order by downsize";
		}
		npage=this.find(page,sql,new Parameter(begin,end));
		return npage;
	}
	public double getDownSizeByType(String begin,String end,String type){
		String sql="select sum(downsize) from stat_ftpurldata where orgtype='"+type+"' and org is null and accessdate >='"+begin+"' and accessdate <'"+end+"'";
		
		//System.out.println("sql"+sql);
		List<Object> data=(List)this.findBySql(sql);
		Object size=(Object)data.get(0);	
		if(size!=null){
			return Double.parseDouble(size.toString());
		}else{
			return 0;
		}
		
	}
	public List<Map<String,String>> getDownSizeAndPv(String begin,String end){
		String sql="select * from ( select b.chnname,round(sum(a.DOWNSIZE)/1024/1024/1024,2) d,sum(a.PVNUMBER) p from stat_ftpurldata a,stat_ftpurlconf b " 
				+" where a.datacode is not null and a.datacode=b.datacode and a.ACCESSDATE>='"+begin+"'"
				+" and a.accessdate<='"+end+"' group by a.datacode,b.chnname order by  d desc,p desc ) where rownum<11";
		List<Object> l=(List)this.findBySql(sql);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<l.size();i++){
			Map map=new HashMap();
			Object[] o=(Object[])l.get(i);
			String name=o[0].toString();
			map.put("name", name);
			String datasize=o[1].toString();
			if(Float.parseFloat(datasize)==0){
				map.put("datasize", "0.01");
			}else{
				map.put("datasize", datasize);
			}
			String num=o[2].toString();
			map.put("num", num);
			list.add(map);
		}
		return list;
	}
	public List<Map<String,String>> getDownSizeAndPv2(String begin,String end){
		String sql="select * from ( select b.chnname,round(sum(a.DOWNSIZE)/1024/1024/1024,2) d,sum(a.PVNUMBER) p from test.stat_ftpurldata a,test.stat_ftpurlconf b "
				+" where a.datacode is not null and a.datacode=b.datacode and a.ACCESSDATE>='"+begin+"'"
				+" and a.accessdate<='"+end+"' group by a.datacode,b.chnname order by  p desc,d desc ) where rownum<11";
		List<Object> l=(List)this.findBySql(sql);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<l.size();i++){
			Map map=new HashMap();
			Object[] o=(Object[])l.get(i);
			String name=o[0].toString();
			map.put("name", name);
			String datasize=o[1].toString();
			if(Float.parseFloat(datasize)==0){
				map.put("datasize", "0.01");
			}else{
				map.put("datasize", datasize);
			}
			String num=o[2].toString();
			map.put("num", num);
			list.add(map);
		}
		return list;
	}
	public List<Map<String,String>> getDownSizeAndPvByDept(String begin,String end){
		String sql="select * from ( select org,round(sum(DOWNSIZE)/1024/1024/1024,2) d,sum(PVNUMBER) p from stat_ftpurldata " 
					+" where org is not null and ACCESSDATE>='"+begin+"'"
					+" and accessdate<='"+end+"' group by org order by  d desc,p desc ) where rownum<11";
		List<Object> l=(List)this.findBySql(sql);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<l.size();i++){
			Map map=new HashMap();
			Object[] o=(Object[])l.get(i);
			String name=o[0].toString();
			map.put("name", name);
			String datasize=o[1].toString();
			if(Float.parseFloat(datasize)==0){
				map.put("datasize", "0.01");
			}else{
				map.put("datasize", datasize);
			}
			String num=o[2].toString();
			map.put("num", num);
			list.add(map);
		}
		return list;
	}
	
	public List getDownSize(String begin, String end, String showType) {
		String sql = null;
	    List<Object> l = null;
	    if ((showType != null) && (!"".equals(showType)))
	    {
	      sql = "select * from (SELECT a.datacode,b.chnname,ROUND(SUM(downsize)/1024/1024,2) downsize FROM stat_ftpurldata a,stat_ftpurlconf b WHERE a.datacode=b.datacode AND downsize!=0 AND accessdate >=:p1 AND accessdate <=:p2 AND a.orgtype=:p3 GROUP BY a.datacode,b.chnname ORDER BY downsize DESC) where rownum<11";
	      
	      l = findBySql(sql, new Parameter(new Object[] { begin, end, showType }));
	    }
	    else
	    {
	      sql = "select * from (SELECT a.datacode,b.chnname,ROUND(SUM(downsize)/1024/1024,2) downsize FROM stat_ftpurldata a,stat_ftpurlconf b WHERE a.datacode=b.datacode AND downsize!=0 AND accessdate >=:p1 AND accessdate <=:p2 GROUP BY a.datacode,b.chnname ORDER BY downsize DESC) where rownum<11";
	      
	      l = findBySql(sql, new Parameter(new Object[] { begin, end }));
	    }
	    List list = new ArrayList();
	    List chnNameList = new ArrayList();
	    List downSizeList = new ArrayList();
	    for (int i = 0; i < l.size(); i++)
	    {
	      Object[] o = (Object[])l.get(i);
	      String dataCode = o[0].toString();
	      String chnName = o[1].toString();
	      Double downSize = Double.valueOf(Double.parseDouble(o[2].toString()));
	      if (downSize.doubleValue() == 0.0D) {
	        downSize = Double.valueOf(0.01D);
	      }
	      chnNameList.add(chnName);
	      downSizeList.add(downSize);
	    }
	    list.add(chnNameList);
	    list.add(downSizeList);
	    return list;
	}
	
	public List getPvNumber(String begin, String end, String showType) {
		String sql = null;
	    List<Object> l = null;
	    if ((showType != null) && (!"".equals(showType)))
	    {
	      sql = "select * from (SELECT a.datacode,b.chnname,SUM(pvnumber) pv FROM stat_ftpurldata a,stat_ftpurlconf b WHERE a.datacode=b.datacode AND downsize!=0 AND accessdate >=:p1 AND accessdate <=:p2 AND a.orgtype=:p3 GROUP BY a.datacode,b.chnname ORDER BY pv DESC) where rownum<11";
	      
	      l = findBySql(sql, new Parameter(new Object[] { begin, end, showType }));
	    }
	    else
	    {
	      sql = "select * from (SELECT a.datacode,b.chnname,SUM(pvnumber) pv FROM stat_ftpurldata a,stat_ftpurlconf b WHERE a.datacode=b.datacode AND downsize!=0 AND accessdate >=:p1 AND accessdate <=:p2 GROUP BY a.datacode,b.chnname ORDER BY pv DESC) where rownum<11";
	      
	      l = findBySql(sql, new Parameter(new Object[] { begin, end }));
	    }
	    List list = new ArrayList();
	    List chnNameList = new ArrayList();
	    List downSizeList = new ArrayList();
	    for (int i = 0; i < l.size(); i++)
	    {
	      Object[] o = (Object[])l.get(i);
	      String dataCode = o[0].toString();
	      String chnName = o[1].toString();
	      int downSize = Integer.parseInt(o[2].toString());
	      chnNameList.add(chnName);
	      downSizeList.add(Integer.valueOf(downSize));
	    }
	    list.add(chnNameList);
	    list.add(downSizeList);
	    return list;
	}
	/*========================FTP下载量统计=====================================*/
	/**
	 * 获取有记录的年份
	 * @return
	 */
	public List<String> getYears() {
		String hql="SELECT substr(f.accessDate,0,4) from  FtpUrlData f group by substr(f.accessDate,0,4) ORDER BY to_number(substr(f.accessDate,0,4))";
		return this.find(hql);
	}
	/**
	 * 按总量   获取总下载量
	 * @return
	 */
	public List<FtpUrlData> getDownLoadSumByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType) {
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("4")) {
			hql="SELECT substring(f.accessDate,1,4),sum(f.downSize) FROM FtpUrlData f WHERE substring(f.accessDate,1,4)  >= ? AND substring(f.accessDate,1,4) <= ? AND f.dataCode is not null GROUP BY substring(f.accessDate,1,4) ORDER BY CONVERT(substring(f.accessDate,1,8),SIGNED) "+orderType;
		}else if(timeType.equals("6")){
			hql="SELECT substring(f.accessDate,1,6),sum(f.downSize) FROM FtpUrlData f WHERE substring(f.accessDate,1,6) >= ? AND substring(f.accessDate,1,6) <= ? AND f.dataCode is not null GROUP BY substring(f.accessDate,1,6) ORDER BY CONVERT(substring(f.accessDate,1,8),SIGNED) "+orderType;
		}else if(timeType.equals("8")){
			hql="SELECT substring(f.accessDate,1,8),sum(f.downSize) FROM FtpUrlData f WHERE substring(f.accessDate,1,8) >= ? AND substring(f.accessDate,1,8) <= ? AND f.dataCode is not null GROUP BY substring(f.accessDate,1,8) ORDER BY CONVERT(substring(f.accessDate,1,8),SIGNED) "+orderType;
		}

		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
			f.setAccessDate(obj[0].toString());
			f.setDownSize(Long.valueOf(obj[1].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
//		return query.list();//List<Object>
	}
	
	
	/**
	 * 按总量-时间获取总下载量
	 * @return
	 */
	public BigDecimal getDownLoadTotalByTime(String start_time,String end_time) {
		String hql="SELECT sum(downSize)  FROM FtpUrlData WHERE accessDate >= ? AND accessDate <= ?";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		return query.uniqueResult()==null?new BigDecimal(0):new BigDecimal(query.uniqueResult().toString());
	}	
	
	/**
	 * 按分类-时间获取下载量
	 * @return
	 */
	public List getDownLoadSumClassByTime(String start_time,String end_time) {
		String sql="SELECT f1.dataCode,f2.chnName,sum(f1.downSize) "
				+ " from STAT_FTPURLDATA f1 left join STAT_FTPURLCONF f2 "
				+ " on f1.dataCode =f2.dataCode "
				+ " where  f1.accessDate >= :p1 AND f1.accessDate <= :p2 "
				+ " AND f1.dataCode is not null "
				+ " GROUP BY f1.dataCode,f2.chnName "
				+ " ORDER BY sum(f1.downSize) desc";
		List<Object[]> list=(List)this.findBySql(sql,new Parameter(start_time,end_time));
		
		List list1=new ArrayList();
		
		List dataCodeList=new ArrayList();
		List chnNameList=new ArrayList();
		List downSizeList=new ArrayList();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			String dataCode=obj[0].toString();
			String chnName=obj[1]==null?"":obj[1].toString();
//			if(obj[0]==null||obj[0].equals("")||obj[1]==null||obj[1].equals("")){
////				obj[0]="其他";
////				obj[1]="其他";
//				dataCode="其他";
//				chnName="其他";
//			}else{
//				dataCode=obj[0].toString();
//				chnName=obj[1].toString();
//			}
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
	 * 按时间分类获取下载量(列表)
	 * @return
	 */
	public List getDownLoadSumClassByTime(String start_time,String end_time,String timeType,String ordername,String ordertype,Integer page,Integer row) {
		String hql="";	
		if(timeType==null||timeType.equals("")){
			timeType = "8";
		}
		if(ordername==null||ordername.equals("")){
			ordername = " sum(downSize) ";
		}
		if(ordertype==null||ordertype.equals("")){
			ordertype = " DESC ";
		}
		
		hql = "SELECT  a.dataCode,b.chnName,sum(a.downSize) "
			+ " FROM STAT_FTPURLDATA a left join STAT_FTPURLCONF b on a.dataCode =b.dataCode "	
			+ " WHERE a.accessDate >= ? AND a.accessDate <= ? "
			+ " AND a.dataCode is not null "
			+ " GROUP BY a.dataCode, b.chnName "
//			+ "	ORDER BY ? "+ordertype;
			+ "	ORDER BY "+ ordername + ordertype;
		Session session = this.getSession();
//		Query query = session.createQuery(hql);
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
//		query.setString(2, ordername);
		
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<Object[]> list=query.list();
		
		List list1=new ArrayList();
		List dataCodeList=new ArrayList();
		List chnNameList=new ArrayList();
		List downSizeList=new ArrayList();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			String dataCode=obj[0].toString();
			String chnName=obj[1]==null?"":obj[1].toString();
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
	
	//分类-
	public List getDownLoadSumClassByTime(String start_time, String end_time, String timeType, Integer page,Integer row, String orderType) {
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		
		String hql="SELECT  a.dataCode,b.chnName,sum(a.downSize) "
				  + " FROM STAT_FTPURLDATA a left join STAT_FTPURLCONF b on a.dataCode =b.dataCode ";
		if(timeType.equals("4")) {
			hql+=" WHERE substr(a.accessDate,0,4) >= ? AND substr(a.accessDate,0,4)<= ? ";
		}else if(timeType.equals("6")){
			hql+=" WHERE substr(a.accessDate,0,6) >= ? AND substr(a.accessDate,0,6)<= ? ";
		}else if(timeType.equals("8")){
			hql+=" WHERE substr(a.accessDate,0,8) >= ? AND substr(a.accessDate,0,8)<= ? ";
		}
		hql+=" AND a.dataCode is not null "
			+ " GROUP BY a.dataCode, b.chnName "
			+ " ORDER BY sum(a.downSize)"+ orderType;
		
//		String hql="";
//		if(timeType.equals("4")) {
//			hql+="SELECT  a.dataCode,b.chnName,sum(a.downSize),substr(a.accessDate,0,4) "
//				+ " FROM STAT_FTPURLDATA a left join STAT_FTPURLCONF b on a.dataCode =b.dataCode "
//				+ " WHERE substr(a.accessDate,0,4) >= ? AND substr(a.accessDate,0,4)<= ? "
//				+ " AND a.dataCode is not null "
//				+ " GROUP BY substr(a.accessDate,0,4),a.dataCode, b.chnName "
//				+ " ORDER BY substr(a.accessDate,0,4),sum(a.downSize)"+ orderType;
//		}else if(timeType.equals("6")){
//			hql+="SELECT  a.dataCode,b.chnName,sum(a.downSize),substr(a.accessDate,0,6) "
//					+ " FROM STAT_FTPURLDATA a left join STAT_FTPURLCONF b on a.dataCode =b.dataCode "
//					+ " WHERE substr(a.accessDate,0,6) >= ? AND substr(a.accessDate,0,6)<= ? "
//					+ " AND a.dataCode is not null "
//					+ " GROUP BY substr(a.accessDate,0,6),a.dataCode, b.chnName "
//					+ " ORDER BY substr(a.accessDate,0,6),sum(a.downSize)"+ orderType;
//		}else if(timeType.equals("8")){
//			hql+="SELECT  a.dataCode,b.chnName,sum(a.downSize),substr(a.accessDate,0,8) "
//					+ " FROM STAT_FTPURLDATA a left join STAT_FTPURLCONF b on a.dataCode =b.dataCode "
//					+ " WHERE substr(a.accessDate,0,8) >= ? AND substr(a.accessDate,0,8)<= ? "
//					+ " AND a.dataCode is not null "
//					+ " GROUP BY substr(a.accessDate,0,8),a.dataCode, b.chnName "
//					+ " ORDER BY substr(a.accessDate,0,8),sum(a.downSize)"+ orderType;
//		}
		
		
		
		Session session = this.getSession();
		Query query = session.createSQLQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		
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
			String chnName=obj[1]==null?"":obj[1].toString();
			long downSize=Long.valueOf(obj[2].toString());
//			String accessDate=obj[3].toString();
			dataCodeList.add(dataCode);
			chnNameList.add(chnName);
			downSizeList.add(downSize);
//			accessDateList.add(accessDate);
		}
		list1.add(dataCodeList);
		list1.add(chnNameList);
		list1.add(downSizeList);
//		list1.add(accessDateList);
		return list1;
	}
	//分类-
		public List getDownLoadSumClassByTime2(String start_time, String end_time, String timeType, Integer page,Integer row, String orderType,String org) {
			orderType = (orderType==null?"DESC":orderType);
			timeType = (timeType==null?"8":timeType);
			//0表示全部
			String hql="SELECT  a.dataCode,b.chnName,sum(a.downSize) "
					  + " FROM test.STAT_FTPURLDATA a left join test.STAT_FTPURLCONF b on a.dataCode =b.dataCode ";
			if(timeType.equals("4")) {
				if("0".equals(org)){
					hql+=" WHERE substr(a.accessDate,0,4) >= ? AND substr(a.accessDate,0,4)<= ? ";
					hql+=" AND a.dataCode is not null and a.org is null"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}else{
					hql+=" WHERE substr(a.accessDate,0,4) >= ? AND substr(a.accessDate,0,4)<= ?  ";
					hql+=" AND a.dataCode is not null and a.org=?"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}
			}else if(timeType.equals("6")){
				if("0".equals(org)){
					hql+=" WHERE substr(a.accessDate,0,6) >= ? AND substr(a.accessDate,0,6)<= ? ";
					hql+=" AND a.dataCode is not null and a.org is null"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}else{
					hql+=" WHERE substr(a.accessDate,0,6) >= ? AND substr(a.accessDate,0,6)<= ? ";
					hql+=" AND a.dataCode is not null and a.org =?"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}
			}else if(timeType.equals("8")){
				if("0".equals(org)){
					hql+=" WHERE substr(a.accessDate,0,8) >= ? AND substr(a.accessDate,0,8)<= ? ";
					hql+=" AND a.dataCode is not null and a.org is null"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}else{
					hql+=" WHERE substr(a.accessDate,0,8) >= ? AND substr(a.accessDate,0,8)<= ? ";
					hql+=" AND a.dataCode is not null and a.org=?"
							+ " GROUP BY a.dataCode, b.chnName "
							+ " ORDER BY sum(a.downSize)"+ orderType;
				}
			}

			Session session = this.getSession();
			Query query = session.createSQLQuery(hql);
			query.setString(0, start_time);
			query.setString(1, end_time);
			if(!"0".equals(org)){
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
//			List accessDateList=new ArrayList();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				String dataCode=obj[0].toString();
				String chnName=obj[1]==null?"":obj[1].toString();
				long downSize=Long.valueOf(obj[2].toString());
//				String accessDate=obj[3].toString();
				dataCodeList.add(dataCode);
				chnNameList.add(chnName);
				downSizeList.add(downSize);
//				accessDateList.add(accessDate);
			}
			list1.add(dataCodeList);
			list1.add(chnNameList);
			list1.add(downSizeList);
//			list1.add(accessDateList);
			return list1;
		}
	
	
	
/**
 * 按省   
 * @param start_time
 * @param end_time
 * @param timeType
 * @param page
 * @param row
 * @param orderType
 * @return
 */
	public List<FtpUrlData> getDownLoadSumProvinceByTime(String start_time, String end_time, String timeType,
			Integer page, Integer row, String orderType) {
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("8")){
			hql=" SELECT f.orgType ,f.org ,sum(f.downSize)  "
			   + " FROM FtpUrlData f "
			   + " WHERE f.accessDate >= ? AND f.accessDate <= ? "
//			   + " AND f.orgType is not null  "
			   + " AND f.orgType ='province' "
			   + " AND f.org is not null "
			   + " GROUP BY f.orgType,f.org "
			   + " ORDER BY f.orgType "+orderType;
		}
		
		Session session = this.getSession();
		//省
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
			f.setorgType(obj[0].toString());
			f.setOrg(obj[1].toString());
			f.setDownSize(Long.valueOf(obj[2].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	
	public List<FtpUrlData> getDownLoadSumProvinceByTime2(String start_time, String end_time, String timeType,
			Integer page, Integer row, String orderType) {
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("8")){
			hql="SELECT f.orgType, sum(f.downSize) "
				+ " FROM FtpUrlData f "
				+ " WHERE f.accessDate >= ? AND f.accessDate <= ? "	
				+ " AND f.orgType ='center' "
				+ " GROUP BY f.orgType "
				+ " ORDER BY f.orgType "+orderType;
		}
		
		Session session = this.getSession();
		//大院总计
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
//			if(obj[1]==null||obj[1].equals("")){
//				f.setDownSize(0l);
//			}else{
//				f.setDownSize(Long.valueOf(obj[1].toString()));	
//			}
			
			f.setorgType(obj[0].toString());
			f.setDownSize(Long.valueOf(obj[1].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	
	public List<FtpUrlData> getDownLoadSumProvinceByTime3(String start_time, String end_time, String timeType,
			Integer page, Integer row, String orderType) {
		
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		String hql="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f ";
		if(timeType.equals("4")){
			hql+=" WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? ";
		}else if(timeType.equals("6")){
			hql+=" WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? ";
		}else if(timeType.equals("8")){
			hql+=" WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? ";
		}
		hql+=" AND f.orgType ='province' "
			+ " AND f.org is not null "
		    + " GROUP BY f.orgType,f.org "
			+ " ORDER BY f.orgType "+orderType;
		
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
			f.setorgType(obj[0].toString());
			f.setOrg(obj[1].toString());
			f.setDownSize(Long.valueOf(obj[2].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	/**
	 * 按省--获取省份
	 * @param orgType
	 * @return
	 */
	public List<String> getOrg(String orgType) {
		String sql="SELECT f.org FROM  STAT_FTPURLDATA f where f.orgType=:p1 and f.org is not null Group By f.org";
		List<String> list =this.findBySql(sql,new Parameter(orgType));
		return list;
	}
	/**
	 * 按省--获取省份
	 * @return
	 */
	public List<String[]> getOrgAll() {
		String sql="SELECT f.org,f.orgtype FROM  STAT_FTPURLDATA f Where  f.org is not Null Group By f.org,f.orgtype Order By orgtype";
		List<String[]> list =this.findBySql(sql);
		return list;
	}
	/**
	 * 按省--根据选择的省份，按年、月、日进行查询
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param org
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<FtpUrlData> getDownLoadSumProvinceByTime(String start_time, String end_time, String timeType,
			String org, Integer page, Integer row, String orderType) {
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		String hql="";
		Session session = this.getSession();
		Query query=null;
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		List<Object[]> list=new ArrayList<Object[]>();
		
		if(org==null||org.equals("")){
			hql+="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f WHERE f.accessDate >= ? AND f.accessDate <= ? AND f.orgType ='province' AND f.org is not null and f.dataCode is null GROUP BY f.orgType,f.org ORDER BY f.orgType "+orderType;
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setorgType(obj[0].toString());
				f.setOrg(obj[1].toString());
				f.setDownSize(Long.valueOf(obj[2].toString()));
				list1.add(f);
			}
		}else if(org.equals(0+"")){//选择全部
			hql+="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f ";
			if(timeType.equals("4")){
				hql+=" WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? "; 
			}else if(timeType.equals("6")){
				hql+=" WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? ";
			}else if(timeType.equals("8")){
				hql+=" WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? ";
			}
			hql+=" AND f.orgType ='province' AND f.org is not null and f.dataCode is null GROUP BY f.orgType,f.org ORDER BY f.orgType "+orderType;
			
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setorgType(obj[0].toString());
				f.setOrg(obj[1].toString());
				f.setDownSize(Long.valueOf(obj[2].toString()));
				list1.add(f);
			}
		}else{
			if(timeType.equals("4")){
				hql+="SELECT substr(f.accessDate,0,4),sum(f.downSize) FROM FtpUrlData f WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? AND f.org =? and f.dataCode is null and f.orgType='province' GROUP BY substr(f.accessDate,0,4) ORDER BY to_number(substr(f.accessDate,0,4)) "+orderType;
			}else if(timeType.equals("6")){
				hql+="SELECT substr(f.accessDate,0,6),sum(f.downSize) FROM FtpUrlData f WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? AND f.org =? and f.dataCode is null and f.orgType='province' GROUP BY substr(f.accessDate,0,6) ORDER BY to_number(substr(f.accessDate,0,6)) "+orderType;
			}else if(timeType.equals("8")){
				hql+="SELECT substr(f.accessDate,0,8),sum(f.downSize) FROM FtpUrlData f  WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? AND f.org =? and f.dataCode is null and f.orgType='province' GROUP BY substr(f.accessDate,0,8) ORDER BY to_number(substr(f.accessDate,0,8)) "+orderType;
			}
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			query.setString(2,org);
			
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setAccessDate(obj[0].toString());
				f.setDownSize(Long.valueOf(obj[1].toString()));
				list1.add(f);
			}
		}
		return list1;//List<FtpUrlData>
	}
	
	
	
	
	
	/**
	 * 按省-时间获取总下载量
	 * @return
	 */
	public BigDecimal getDownLoadProvinceByTime(String start_time,String end_time) {
		String hql="SELECT sum(downSize)  "
				+ " FROM FtpUrlData "
				+ " WHERE accessDate >= ? AND accessDate <= ? "
				+ " AND orgType is not null  ";
//				+ " AND orgType ='center' or orgType ='province'  ";
				
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		return query.uniqueResult()==null?new BigDecimal(0):new BigDecimal(query.uniqueResult().toString());
	}
	/**
	 * 按 大院用户机构
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<FtpUrlData> getDownLoadSumCenterByTime(String start_time, String end_time, String timeType,
			Integer page, Integer row, String orderType) {
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		String hql="";
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		if(timeType.equals("8")){
			hql=" SELECT f.orgType ,f.org ,sum(f.downSize) "
			   + " FROM FtpUrlData f "
			   + " WHERE f.accessDate >= ? AND f.accessDate <= ? "
			   + " AND f.orgType ='center'  "
			   + " AND f.org  is not null "
			   + " GROUP BY f.orgType,f.org "
			   + " ORDER BY f.orgType "+orderType;
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
			f.setorgType(obj[0].toString());
			f.setOrg(obj[1].toString());
			f.setDownSize(Long.valueOf(obj[2].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	
	public List<FtpUrlData> getDownLoadSumCenterByTime2(String start_time, String end_time, String timeType,
			Integer page, Integer row, String orderType) {
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		String hql="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f ";
		
		if(timeType.equals("4")){
			hql+=" WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? ";
		}else if(timeType.equals("6")){
			hql+=" WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? ";
		}else if(timeType.equals("8")){
			hql+=" WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? ";
		}
		
		hql+=" AND f.orgType ='center' "
			+ " AND f.org  is not null "
			+ " GROUP BY f.orgType,f.org "
			+ " ORDER BY f.orgType "+orderType;
		
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0,start_time);
		query.setString(1,end_time);
		if(page!=null&&row!=null){
			query.setFirstResult((page-1)*row);
			query.setMaxResults(row);
		}
		List<Object[]> list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			FtpUrlData f=new FtpUrlData();
			f.setorgType(obj[0].toString());
			f.setOrg(obj[1].toString());
			f.setDownSize(Long.valueOf(obj[2].toString()));
			list1.add(f);
		}
		return list1;//List<FtpUrlData>
	}
	/**
	 * 按大院--根据选择的大院，按年、月、日进行查询
	 * @param start_time
	 * @param end_time
	 * @param timeType
	 * @param org
	 * @param page
	 * @param row
	 * @param orderType
	 * @return
	 */
	public List<FtpUrlData> getDownLoadSumCenterByTime(String start_time, String end_time, String timeType, String org,
			Integer page, Integer row, String orderType) {
		orderType = (orderType==null?"DESC":orderType);
		timeType = (timeType==null?"8":timeType);
		String hql="";
		
		Session session = this.getSession();
		Query query=null;
		List<FtpUrlData> list1=new ArrayList<FtpUrlData>();
		List<Object[]> list=new ArrayList<Object[]>();
		
		if(org==null||org.equals("")){
			hql+="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f WHERE f.accessDate >= ? AND f.accessDate <= ? AND f.orgType ='center' AND f.org is not null GROUP BY f.orgType,f.org ORDER BY f.orgType "+orderType;
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setorgType(obj[0].toString());
				f.setOrg(obj[1].toString());
				f.setDownSize(Long.valueOf(obj[2].toString()));
				list1.add(f);
			}
		}else if(org.equals(0+"")){//选择全部
			hql+="SELECT f.orgType ,f.org ,sum(f.downSize) FROM FtpUrlData f "; 
			if(timeType.equals("4")){
				hql+=" WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? ";
			}else if(timeType.equals("6")){
				hql+=" WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? ";
			}else if(timeType.equals("8")){
				hql+=" WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? ";
			}
			hql+=" AND f.orgType ='center' AND f.org is not null GROUP BY f.orgType,f.org ORDER BY f.orgType "+orderType;
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setorgType(obj[0].toString());
				f.setOrg(obj[1].toString());
				f.setDownSize(Long.valueOf(obj[2].toString()));
				list1.add(f);
			}
		}else{
			if(timeType.equals("4")){
				hql+="SELECT substr(f.accessDate,0,4),sum(f.downSize) FROM FtpUrlData f WHERE substr(f.accessDate,0,4) >= ? AND substr(f.accessDate,0,4)<= ? AND f.org =? and f.orgType='center' GROUP BY substr(f.accessDate,0,4) ORDER BY to_number(substr(f.accessDate,0,4)) "+orderType;
			}else if(timeType.equals("6")){
				hql+="SELECT substr(f.accessDate,0,6),sum(f.downSize) FROM FtpUrlData f WHERE substr(f.accessDate,0,6) >= ? AND substr(f.accessDate,0,6)<= ? AND f.org =? and f.orgType='center' GROUP BY substr(f.accessDate,0,6) ORDER BY to_number(substr(f.accessDate,0,6)) "+orderType;
			}else if(timeType.equals("8")){
				hql+="SELECT substr(f.accessDate,0,8),sum(f.downSize) FROM FtpUrlData f  WHERE substr(f.accessDate,0,8) >= ? AND substr(f.accessDate,0,8)<= ? AND f.org =? and f.orgType='center' GROUP BY substr(f.accessDate,0,8) ORDER BY to_number(substr(f.accessDate,0,8)) "+orderType;
			}
			query = session.createQuery(hql);
			query.setString(0,start_time);
			query.setString(1,end_time);
			query.setString(2,org);
			
			if(page!=null&&row!=null){
				query.setFirstResult((page-1)*row);
				query.setMaxResults(row);
			}
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=list.get(i);
				FtpUrlData f=new FtpUrlData();
				f.setAccessDate(obj[0].toString());
				f.setDownSize(Long.valueOf(obj[1].toString()));
				list1.add(f);
			}
		}
		
		return list1;
	}
	
	
	
	/**
	 * 按 大院用户机构
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public BigDecimal getDownLoadCenterByTime(String start_time,String end_time) {
		String hql="SELECT sum(downSize)  "
				+ " FROM FtpUrlData "
				+ " WHERE accessDate >= ? AND accessDate <= ? "
				+ " AND orgType ='center'  ";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, start_time);
		query.setString(1, end_time);
		return query.uniqueResult()==null?new BigDecimal(0):new BigDecimal(query.uniqueResult().toString());
	}
	/*================================================================*/
}
