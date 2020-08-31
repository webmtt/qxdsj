package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataSearchDef2;

@Repository
public class DataSearchDefDao2 extends BaseDao<DataSearchDef2>{

	public Page<DataSearchDef2> getByPage(Page<DataSearchDef2> page, String datacode) {
		String sql="from DataSearchDef2 where datacode like :p1 ";
		return this.find(page,sql,new Parameter("%"+datacode+"%"));
	}
	public List<DataSearchDef2> getListByDataCode(String datacode) {
		if(datacode==null||"".equals(datacode)){
			String sql="from DataSearchDef2 ";
			return this.find(sql);
		}else{
			String sql="from DataSearchDef2 where  datacode=:p1 ";
			return this.find(sql,new Parameter(datacode));
		}
		
		
	}
	public List<DataSearchDef2> getListByDatapCode(String datacode) {
		if(datacode==null||"".equals(datacode)){
			String sql="from DataSearchDef2 ";
			return this.find(sql);
		}else{
			String sql="from DataSearchDef2 where parentdatacode=:p1 ";
			return this.find(sql,new Parameter(datacode));
		}
		
		
	}
	public List<DataSearchDef2> getListByCodeFlag(String code,String flag) {
		if("0".equals(flag)){
			String sql="from DataSearchDef2 where  searchsetcode=:p1";
			return this.find(sql,new Parameter(code));
		}else if("1".equals(flag)){
			String sql="from DataSearchDef2 where  elesetcode=:p1 ";
			return this.find(sql,new Parameter(code));
		}else if("1".equals(flag)){
			String sql="from DataSearchDef2 where  datacode=:p1 ";
			return this.find(sql,new Parameter(code));
		}
		return null;
	}
	public Page<DataSearchDef2> getByPage(Page<DataSearchDef2> page) {
		String sql="from DataSearchDef2 ";
		return this.find(page,sql,new Parameter());
	}

	public DataSearchDef2 getDataSearchDefByDataCode(String datacode) {
		String sql="from DataSearchDef2 where datacode =:p1 ";
		List<DataSearchDef2> list =this.find(sql,new Parameter(datacode));
		return list.get(0);
	}
	public void updateDataSearchDefByDataCode(String datacode,String interfacesetcode) {
		String sql="update DataSearchDef2 set interfacesetcode=:p1 where datacode =:p2 ";
		this.update(sql, new Parameter(interfacesetcode,datacode));
	}
	public void delDataSearchDefByDataCode(String datacode) {
		String sql="delete from DataSearchDef2 where datacode =:p1 ";
		this.update(sql, new Parameter(datacode));
	}
	
}
