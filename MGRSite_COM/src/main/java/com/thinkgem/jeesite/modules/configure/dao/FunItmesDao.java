/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.configure.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.configure.entity.FunItmes;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class FunItmesDao extends BaseDao<FunItmes>{
	public List<FunItmes> getFunItmesList(int layer, String area){
		return this.find("from FunItmes where layer=:p1 and invalid = 0 and areaItem like :p2 order by orderno",new Parameter(layer,"%"+area+"%"));
	}
	public List<FunItmes> getFunItemsListParentIDZero(){
		return this.find("from FunItmes where parentid = 0 and invalid = 0 and areaItem like '%,1,%' order by orderno");
	}

	public List<FunItmes> findAllList(String areaItem){
		List<FunItmes> sourcelist=this.find("from FunItmes where areaItem like :p1 and invalid=0  order by orderNo",new Parameter("%,"+areaItem+",%"));
		List<FunItmes> list=new ArrayList<FunItmes>();
		for(int i=0;i<sourcelist.size();i++){
			FunItmes funItmes=sourcelist.get(i);
			if(funItmes.getLayer()==1){
				FunItmes funItmestemp=new FunItmes();
				funItmestemp.setId(0);
				funItmestemp.setLayer(0);
				funItmestemp.setInvalid(0);
				funItmestemp.setName("顶级菜单");
				funItmes.setParent(funItmestemp);
				list.add(funItmes);
			}else{
				list.add(funItmes);
			}
			
		}
		FunItmes funItmestemp=new FunItmes();
		funItmestemp.setId(0);
		funItmestemp.setLayer(0);
		funItmestemp.setInvalid(0);
		funItmestemp.setName("顶级菜单");
		list.add(funItmestemp);
		return list;
	}
}
