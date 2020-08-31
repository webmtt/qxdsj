package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;

/**
 * 公共参数DAO接口
 * @author yb
 * @version 2014-12-19
 */
@Repository
public class ComparasDao extends BaseDao<Comparas> {
	public Object getComparasByKey(String key){
		
		List<Comparas> list=this.find("from Comparas where keyid=:p1",new Parameter(key));
		if(list.size()>0){
			Comparas comparas=list.get(0);
			if(comparas.getType().equals("String")){
				return comparas.getStringvalue();
			}
			if(comparas.getType().equals("int")){
				return comparas.getIntvalue();
			}
			if(comparas.getType().equals("boolean")){
				return comparas.getBooleanvalue();
			}
		}
		return null;
		
	}

	@Override
	public void save(Comparas comparas) {
		// TODO Auto-generated method stub
		super.save(comparas);
	}
	@Override
	public void save(List<Comparas> dataList) {
		// TODO Auto-generated method stub
		super.save(dataList);
	}
	@Override
	public List<Comparas> findAll() {
		// TODO Auto-generated method stub
		return super.findAll();
	}
	public Page<Comparas> findComparasPage(String description,Page<Comparas> page, Comparas comparas) {
		
		StringBuffer sb=new StringBuffer("from Comparas where 1=1 ");
		if(description!=null&&!"".equals(description)){
			sb.append(" and description like '%"+description+"%'");
		}
//		System.out.println("===========dd"+sb.toString());
		return this.find(page,sb.toString(),new Parameter());
		
	}

	public Comparas findComparas(String keyid) {
		Comparas comparas=new Comparas();
		String sql="from Comparas where keyid=:p1";
		List<Comparas> list=this.find(sql, new Parameter(keyid));
		if(list.size()>0){
			comparas=list.get(0);
		}
		return comparas;
	}
	public void updateComparasById(String keyid,String stringvalue) {
		update("update Comparas set stringvalue=:p1 where keyid=:p2", new Parameter(stringvalue,keyid));
	}
	
}
