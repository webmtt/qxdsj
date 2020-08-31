package com.thinkgem.jeesite.modules.index.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.FuncItmesDef;
import com.thinkgem.jeesite.modules.index.entity.FuncItmes;

@Repository
public class FuncItmesDefDao extends BaseDao<FuncItmesDef> {
	
	public List<FuncItmesDef> findAll(){
		//return this.find("from FuncItmesDef where invalid = 0 and isNosearch = 0 order by parentID,orderNo");
		return this.find("from FuncItmesDef where isNosearch = 0 order by parentID,orderNo");
	}
	
	public List<FuncItmes> findFuncItmesAll(){
		//return this.find("from FuncItmes where isNosearch = 0 and invalid = 0 order by parentID,orderNo");
		return this.find("from FuncItmes where isNosearch = 0 order by parentID,orderNo");
	}
	
	public FuncItmes findFuncItmesByFuncID(int funcItemID){
		//String sql = "from FuncItmes where invalid = 0 and isNosearch = 0 and funcItemID =:p1 order by funcItemID||parentid,orderNo";
		String sql = "from FuncItmes where isNosearch = 0 and funcItemID =:p1 order by funcItemID||parentid,orderNo";
		List<FuncItmes> list = this.find(sql, new Parameter(funcItemID));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public FuncItmesDef findFuncItmesDefByFuncID(int funcItemID){
		//String sql = "from FuncItmesDef where invalid = 0 and isNosearch = 0 and funcItemID =:p1 order by funcItemID||parentid,orderNo";
		String sql = "from FuncItmesDef where isNosearch = 0 and funcItemID =:p1 order by funcItemID||parentid,orderNo";
		List<FuncItmesDef> list = this.find(sql, new Parameter(funcItemID));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<FuncItmesDef> findFuncItmesDefByParentID(int parentID){
		String sql = "from FuncItmesDef where isNosearch = 0 and parentID =:p1 order by orderNo,funcItemID";
		List<FuncItmesDef> list = this.find(sql, new Parameter(parentID));
		return list;
	}
	
	public void deleteFuncItmesDefByFuncID(int funcItemID){
		String sql = "delete from FuncItmesDef where funcItemID =:p1";
		this.update(sql, new Parameter(funcItemID));
	}
	
	public Page<FuncItmesDef> getByPage(Page<FuncItmesDef> page, String CHNName){
		Page<FuncItmesDef> p = new Page<FuncItmesDef>();
		String sql = "";
		if(CHNName!=null && !"".equals(CHNName)){
			//sql = "from FuncItmesDef where invalid = 0 and isNosearch = 0 and CHNName like :p1 order by parentID,orderNo";
			sql = "from FuncItmesDef where isNosearch = 0 and CHNName like :p1 order by parentID,orderNo";
			p = this.find(page, sql, new Parameter("%"+CHNName.trim()+"%"));
		}else{
			//sql = "from FuncItmesDef where invalid = 0 and isNosearch = 0 order by parentID,orderNo";
			sql = "from FuncItmesDef where isNosearch = 0 order by parentID,orderNo";
			p = this.find(page, sql);
		}
		
		return p;
	}
	
	public Page<FuncItmesDef> searchByPage(Page<FuncItmesDef> page, String CHNName){
		Page<FuncItmesDef> p = new Page<FuncItmesDef>();
		String sql = "from FuncItmesDef where isNosearch = 0 and CHNName like :p1 order by parentID,orderNo";
		p = this.find(page, sql, new Parameter("%"+CHNName.trim()+"%"));
		return p;
	}
	
	public Integer getMaxFuncItemID(Integer parentID){
		String sql = "from FuncItmesDef where parentID =:p1 order by funcItemID desc";
		List<FuncItmesDef> list = this.find(sql, new Parameter(parentID));
		if(list!=null && list.size()>0){
			return list.get(0).getFuncItemID();
		}else{
			return Integer.valueOf(parentID.toString()+"00");
		}
	}
	
	public Integer getMaxOrderNo(Integer parentID){
		String sql = "from FuncItmesDef where parentID =:p1 order by orderNo desc";
		List<FuncItmesDef> list = this.find(sql, new Parameter(parentID));
		if(list!=null && list.size()>0){
			return list.get(0).getOrderNo();
		}else{
			return 0;
		}
	}
	
	public Integer getLayer(Integer funcItemID){
		String sql = "from FuncItmesDef where funcItemID =:p1";
		List<FuncItmesDef> list = this.find(sql, new Parameter(funcItemID));
		return list.get(0).getLayer();
	}
	
}
