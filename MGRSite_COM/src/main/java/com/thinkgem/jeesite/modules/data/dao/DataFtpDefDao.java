package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataFtpDef;

@Repository
public class DataFtpDefDao extends BaseDao<DataFtpDef>{
	public List<DataFtpDef> findRow(){
		return this.find("from DataFtpDef where invalid = '0' and datacode='UPAR_ARD_GLB_FTM_CHNGTS'");
	}

	public Page<DataFtpDef> getDataDefPage(String dataCode, Page<DataFtpDef> page) {
		String sql="from DataFtpDef where datacode=:p1";
		return this.find(page, sql, new Parameter(dataCode));
	}

	public List<DataFtpDef> findDataFtpDefByCode(String dataCode) {
		String sql="from DataFtpDef where datacode=:p1 ";
		return this.find(sql, new Parameter(dataCode));
	}
	@Override
	public void save(DataFtpDef entity) {
		super.save(entity);
	}

	public void delDataFtpDef(String datacode) {
		String sql="delete from  DataFtpDef where datacode=:p1";
		this.update(sql, new Parameter(datacode));
	}
	public Page<Object[]> findPageByName2(Page<Object[]> page,String categoryid,String dataCode){
		if(dataCode==null||"".equals(dataCode)||"0".equals(dataCode)){
			StringBuffer sb=new StringBuffer("Select a.datacode,a.chnname,b.ftpnames From bmd_datadef a right Join "+
					 " ( Select * From BMD_DATAFTPDEF Where invalid=0) b on "+
			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "+
			         " And b.invalid=0 And a.invalid=0 Order By datacode");
			return this.findBySql(page,sb.toString(),new Parameter(categoryid));
		}else{
			StringBuffer sb=new StringBuffer("Select a.datacode,a.chnname,b.ftpnames From bmd_datadef a right Join "+
					 " ( Select * From BMD_DATAFTPDEF Where invalid=0) b on "+
			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1 "+
			         " and datacode=:p2) "+
			         " And b.invalid=0 And a.invalid=0 Order By datacode");
			return this.findBySql(page,sb.toString(),new Parameter(categoryid,dataCode));
		}
	}
	public Page<Object[]> findPageByName(Page<Object[]> page,String categoryid,String dataCode){
		if(dataCode==null||"".equals(dataCode)||"0".equals(dataCode)){
			StringBuffer sb=new StringBuffer("Select a.datacode,a.chnname,b.ftpnames,b.invalid From bmd_datadef a right Join "+
					 " ( Select * From BMD_DATAFTPDEF) b on "+
			         " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "+
			         "  Order By datacode");
			return this.findBySql(page,sb.toString(),new Parameter(categoryid));
		}else{
			StringBuffer sb=new StringBuffer("Select a.datacode,a.chnname,b.ftpnames,b.invalid From bmd_datadef a right Join "+
					 " ( Select * From BMD_DATAFTPDEF) b on "+
			         " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1 "+
			         " and datacode=:p2) "+
			         " Order By datacode");
			return this.findBySql(page,sb.toString(),new Parameter(categoryid,dataCode));
		}
	}
}
