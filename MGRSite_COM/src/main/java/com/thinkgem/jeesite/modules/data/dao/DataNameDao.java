
package com.thinkgem.jeesite.modules.data.dao;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;


/**
 * 用户DAO接口
 */
@Repository
public class DataNameDao extends BaseDao<DataCategory> {
	/**
	 * 查询资料大类
	 * @return
	 */
	
	public List<DataCategoryDef> datalist1(Integer parentid){
		return this.find("from DataCategoryDef where parentid=:p1 and invalid='0' order by OrderNo asc",new Parameter(parentid));
//		String hql="";
//		hql="from DataCategoryDef where parentid='0' and invalid='0' order by categoryid asc";
//		List<DataCategoryDef> list=this.find(hql);
//		if(list!=null){
//			System.out.println(list.get(0));
//			return list;
//		}else{
//			return null;
//		}
	}
	/**
	 * 查询二级资料
	 * @return
	 */
	public List datalist2(){
		List<DataCategoryDef> lists = this.find("from DataCategoryDef where parentid=0 and invalid='0' order by OrderNo asc");
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<lists.size();i++){
			ids.add(lists.get(i).getCategoryid());
		}
		String hql = "";
		List list= new ArrayList();
		for(int j=0;j<ids.size();j++){
			hql="select CHNNAME from BMD_DATACATEGORYDEF where PARENTID =:p1 and INVALID=0 order by OrderNo asc";
			List<Object[]> objs = this.findBySql(hql,new Parameter(ids.get(j)));
			list.addAll(objs);
		}
		
		//String hql=""
		//hql="select CHNNAME from BMD_DATACATEGORYDEF where PARENTID IN (select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID=0 and INVALID=0) and INVALID=0 order by CATEGORYID asc";
		//List list=this.findBySql(hql);
		return list;
	}
	/**
	 * 查询资料名称
	 * @return
	 */
	public List<Object[]> datalist3(){
		/**
		String hql="";
		//hql="select CHNNAME from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID in (select CATEGORYID from BMD_CATEGORYDATARELT group BY CATEGORYID) and a.datacode=b.datacode and a.INVALID=0  order by CATEGORYID asc;";
		//copy
		// hql="select a.CHNNAME from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID in (select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID!=0 and INVALID=0) and a.datacode=b.datacode and a.INVALID=0 order by b.CATEGORYID asc";
		hql="select a.CHNNAME,a.DataCode,a.UpdateFreq,a.ProduceTime,a.SpatialResolution,a.ReferSystem,a.KeyWords,a.PublishTime,a.AreaScope,a.NorthLat,a.SouthLat,a.EastLon,a.WestLon,a.DataBeginTime,a.DataEndTime,a.ObsFreq,a.Producer,a.ProductionUnit,a.ContactInfo from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID in (select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID!=0 and INVALID=0) and a.datacode=b.datacode and a.INVALID=0 order by b.CATEGORYID asc";
		List<Object[]> list= this.findBySql(hql);
		return list;
		*/
		List<Object[]> list = new ArrayList<Object[]>();
		String hql = "";
		List<DataCategoryDef> lists = this.find("from DataCategoryDef where parentid=0 and invalid='0' order by OrderNo asc");
		//资料大类ids
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<lists.size();i++){
			ids.add(lists.get(i).getCategoryid());
		}
		hql="select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID=:p1 and INVALID=0 order by OrderNo asc";
		//二级资料ids
		List<Integer> twoIds = new ArrayList<Integer>();
		for(int j=0;j<ids.size();j++){
			List<Integer> objs = this.findBySql(hql,new Parameter(ids.get(j)));
			twoIds.addAll(objs);
		}
		hql = "select a.CHNNAME,a.UDataCode,a.UpdateFreq,a.ProduceTime,a.SpatialResolution,a.ReferSystem,a.KeyWords,a.PublishTime,a.AreaScope,a.NorthLat,a.SouthLat,a.EastLon,a.WestLon,a.DataBeginTime,a.DataEndTime,a.ObsFreq,a.Producer,a.ProductionUnit,a.ContactInfo from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID = :p1 and a.datacode=b.datacode and a.INVALID=0 order by b.OrderNo asc";
		for(int k=0;k<twoIds.size();k++){
			List<Object[]> list2  = this.findBySql(hql,new Parameter(twoIds.get(k)));
			list.addAll(list2);
		}
		return list;
	}
	
	/**
	 * 查询资料大类下资料个数
	 * @return
	 */
	public List dataSum(){
		String hql="";
		hql="select CATEGORYID a from BMD_DATACATEGORYDEF where PARENTID=0 and INVALID=0 order by OrderNo asc";
		List list=this.findBySql(hql);
		hql="select count(CATEGORYID) a from BMD_DATACATEGORYDEF where PARENTID=0 and INVALID=0 order by CATEGORYID asc";
		List list1=this.findBySql(hql);
		hql="select count(a.CHNNAME) from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID in (select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID=:p1 and INVALID=0) and a.datacode=b.datacode and a.INVALID=0  order by b.CATEGORYID asc";
		List list3 = new ArrayList();
		for(int i=0;i<Integer.parseInt(list1.get(0).toString());i++){
			List list2=this.findBySql(hql,new Parameter(list.get(i)));
			list3.add(list2);
		}
		return list3;
	}
	/**
	 * 查询二级资料个数
	 * @return
	 */
	public List dataSum2(){
		/**
		String hql="";
		//hql="select CHNNAME from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID in (select CATEGORYID from BMD_CATEGORYDATARELT group BY CATEGORYID) and a.datacode=b.datacode and a.INVALID='0'  order by CATEGORYID asc;";
		hql="select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID!=0 and INVALID=0 order by CATEGORYID asc";
		List list=this.findBySql(hql);
		hql="select count(CATEGORYID) from BMD_DATACATEGORYDEF where PARENTID!=0 and INVALID=0 order by CATEGORYID asc";
		List list1=this.findBySql(hql);
		hql="select count(a.CHNNAME) from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID=:p1 and a.datacode=b.datacode and a.INVALID=0 order by b.CATEGORYID asc";
		List list3=new ArrayList();
		for(int i=0;i<Integer.parseInt(list1.get(0).toString());i++){
			List list2=this.findBySql(hql,new Parameter(list.get(i)));
			list3.add(list2);
		}
		
		return list3;
		*/
		String hql = "";
		List<DataCategoryDef> lists = this.find("from DataCategoryDef where parentid=0 and invalid='0' order by OrderNo asc");
		//资料大类ids
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<lists.size();i++){
			ids.add(lists.get(i).getCategoryid());
		}
		hql="select CATEGORYID from BMD_DATACATEGORYDEF where PARENTID=:p1 and INVALID=0 order by OrderNo asc";
		//二级资料ids
		List<Integer> twoIds = new ArrayList<Integer>();
		for(int j=0;j<ids.size();j++){
			List<Integer> objs = this.findBySql(hql,new Parameter(ids.get(j)));
			twoIds.addAll(objs);
		}
		hql="select count(a.CHNNAME) from BMD_DATADEF a,BMD_CATEGORYDATARELT b where  b.CATEGORYID = :p1 and a.datacode=b.datacode and a.INVALID=0 order by b.CATEGORYID asc ";//  order by b.OrderNo asc
		//每个二级资料对应的n个详细资料
		List list3 = new ArrayList();
		for(int i=0;i<twoIds.size();i++){
			List list2=this.findBySql(hql,new Parameter(twoIds.get(i)));
			list3.add(list2);
		}	
		return list3;
	}
}
