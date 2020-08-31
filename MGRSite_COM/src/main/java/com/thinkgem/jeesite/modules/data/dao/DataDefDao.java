package com.thinkgem.jeesite.modules.data.dao;

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
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.entity.DataDef;

@Repository
public class DataDefDao extends BaseDao<DataDef>{
	public List<DataDef> findAll(){
		return this.find("from DataDef  order by orderno");
	}
	public DataDef findDataDefByDataCode(String dataCode){
		List<DataDef> list = this.find("from DataDef where datacode =:p1  order by orderno",new Parameter(dataCode));
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<DataDef> findDataDefByCodes(List<CategoryDataRelt> list){
		StringBuffer inSql = new StringBuffer();
		StringBuffer orderSql = new StringBuffer();
		List<DataDef> dataDefList = new ArrayList();
		if(list.size() > 0){
			for(int i=0;i<list.size();i++){
				if(i != list.size()-1){
					inSql.append(list.get(i).getDatacode()).append("','");
				}else{
					inSql.append(list.get(i).getDatacode());
				}
			}
			
			for(int i=0;i<list.size();i++){
				if(i != list.size()-1){
					orderSql.append("'"+list.get(i).getDatacode()+"',"+ (i+1)+",");
				}else{
					orderSql.append("'"+list.get(i).getDatacode()+"',"+ (i+1));
				}
			}			
			String sql = "from DataDef where  datacode in ('"+inSql.toString()+"') order by decode(datacode,"+orderSql.toString()+")";

			
			Session session= this.getSession();
			Query query=session.createQuery(sql);

			dataDefList = query.list();
		}

		return dataDefList;

	}
	public Page<DataDef> getSearchResultByKey(String keyWord, String subcontent, Page<DataDef> page) {
		String sql="from DataDef where chnname like :p1 ";
		if(subcontent==null||"".equals(subcontent)){
			return this.find(page, sql,new Parameter(keyWord));		
		}else{
			sql+=" and chnname like :p2";
			return this.find(page, sql,new Parameter(keyWord,subcontent));	
		}
	}
	public Page<DataDef> getSearchResultByKey2(String keyWord, Page<DataDef> page) {
		String sql="from DataDef where 1=1 and invalid='0'";
		if(keyWord!=null&&!"".equals(keyWord)){
			sql=sql+" and chnname like :p1";
			return this.find(page, sql,new Parameter(keyWord));		
		}else{
			return this.find(page, sql);	
		}
	}
	public List<DataDef> getTotalPageByKey(String keyWord, String subcontent) {
		String sql="from DataDef where chnname like :p1 and invalid='0'";
		if(subcontent==null||"".equals(subcontent)){
			return this.find(sql,new Parameter(keyWord));			
		}else{
			sql+=" and chnname like :p2";
			return this.find(sql,new Parameter(keyWord,subcontent));	
		}
	}
	public List<String> getSearchResultByKey(String keyWord) {
		return this.find("from DataDef where chnname like :p1 and  invalid='0'", new Parameter(keyWord));
	}
	public void delDataDef(String id){
//		String sql="delete from  DataDef  where datacode=:p1";
		String sql="update DataDef set invalid=1 where datacode=:p1";
		this.update(sql,new Parameter(id));
	}
	
	@Override
	public void save(DataDef entity) {
		super.save(entity);
	}
	/**
	public Page fondDataDefByCodes(String categoryid, String dataDefName, Page<DataDef> page) {
		String sql="Select b.dataCode,b.chnName,b.chnDescription,b.serviceMode,a.Orderno,b.invalid From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode and b.invalid=0 and a.categoryid=:p1 ";
		if(dataDefName!=null&&!"".equals(dataDefName)){
			sql+=" and b.chnname like '%"+dataDefName+"%'";
		}
		sql+=" Order By a.Orderno";
		
		return this.findBySql(page, sql,new Parameter(categoryid));
	}
	public List fondDataDefListByCodes(String categoryid, String dataDefName) {
		String sql="Select b.dataCode,b.chnName,b.chnDescription,b.serviceMode,a.Orderno,b.invalid From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode and b.invalid=0 and a.categoryid=:p1 ";
		if(dataDefName!=null&&!"".equals(dataDefName)){
			sql+=" and b.chnname like '%"+dataDefName+"%'";
		}
		sql+=" Order By a.Orderno";
		
		return this.findBySql(sql,new Parameter(categoryid));
	}
	*/
	public Page fondDataDefByCodes(String categoryid, String dataDefName, Page<DataDef> page) {
		String sql="Select b.dataCode,b.chnName,b.chnDescription,b.serviceMode,a.Orderno,b.invalid From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode  and a.categoryid=:p1 ";
		if(dataDefName!=null&&!"".equals(dataDefName)){
			sql+=" and b.chnname like '%"+dataDefName+"%'";
		}
		sql+=" Order By a.Orderno";
		
		return this.findBySql(page, sql,new Parameter(categoryid));
	}
	public List fondDataDefListByCodes(String categoryid, String dataDefName) {
		String sql="Select b.dataCode,b.chnName,b.chnDescription,b.serviceMode,a.Orderno,b.invalid From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode  and a.categoryid=:p1 ";
		if(dataDefName!=null&&!"".equals(dataDefName)){
			sql+=" and b.chnname like '%"+dataDefName+"%'";
		}
		sql+=" Order By a.Orderno";
		
		return this.findBySql(sql,new Parameter(categoryid));
	}

	public void upload(List<List<Object>> list){
		for(int i = 0; i < list.size(); i++){
			List<Object> ob = list.get(i);
			String sql1 =
					"insert into BMD_CATEGORYDATARELT(id,categoryid,datacode,orderno)values( '"
							+ob.get(0)
							+"','"
							+ob.get(1)
							+"','"
							+ob.get(2)
							+"','"
							+ob.get(33)
							+"')";
			this.saveTest(sql1);

			String sql2 =
					"insert into BMD_DATADEF(datacode,cdatacode,udatacode,dataclasscode,chnname,shortchnname,chndescription2," +
							"keywords,updatefreq,spatialresolution,refersystem,producetime,areascope,westlon,eastlon,northlat," +
							"southlat,databegintime,dataendtime,dataendflag,obsfreq,producer,productionunit,contactinfo," +
							"storagetype,searchurl,insertmode,servicemode,datasourcecode,isincludesub,userrankid," +
							"orderno,invalid,imageurl,optionmetagroup,verticallowest,verticalhighest,verticalunit," +
							"verticalbase,chndescription,publishTime,ftpaccesstype,isoffline,offlininfo,timezone)values( '"
							+ob.get(2) +"','" +ob.get(3) +"','" +ob.get(4) +"','" +ob.get(5)+"','"+ob.get(6)+"','"+ob.get(7)+"','"+ob.get(8)
							+"','"+ob.get(9)+"','"+ob.get(10)+"','"+ob.get(11)+"','"+ob.get(12)+"','"+ob.get(13)+"','"+ob.get(14)+"','"+ob.get(15)
							+"','"+ob.get(16)+"','"+ob.get(17)+"','"+ob.get(18)+"','"+ob.get(19)+"','"+ob.get(20)+"','"+ob.get(21)
							+"','"+ob.get(22)+"','"+ob.get(23)+"','"+ob.get(24)+"','"+ob.get(25)+"','"+ob.get(26)+"','"+ob.get(27)
							+"','"+ob.get(28)+"','"+ob.get(29)+"','"+ob.get(30)+"','"+ob.get(31)+"','"+ob.get(32)+"','"+ob.get(33)
							+"','"+ob.get(34)+"','"+ob.get(35)+"','"+ob.get(36)+"','"+ob.get(37)+"','"+ob.get(38)+"','"+ob.get(39)
							+"','"+ob.get(40)+"','"+ob.get(41)+"','"+ob.get(42)+"','"+ob.get(43)+"','"+ob.get(44)+"','"+ob.get(45)
							+"','"+ob.get(46)
							+"')";
			this.saveTest(sql2);

		}
	}
	private void saveTest(String sql) {
		this.getSession().createSQLQuery(sql).executeUpdate();
		this.getSession().flush(); // 清理缓存，执行批量插入
		this.getSession().clear(); // 清空缓存中的 对象
	}

	public List<DataDef> findDataDefByParentId(Integer id) {
		String sql="Select b.dataCode,b.chnName,b.chnDescription,b.serviceMode,b.invalid,b.orderno From bmd_datadef b,bmd_categorydatarelt a Where a.datacode=b.datacode  and a.categoryid=:p1 ";

		sql+=" Order By b.Orderno";

		return this.find(sql,new Parameter(id));
	}
}
