/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRull;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRullModel;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class PortalImageRullDao extends BaseDao<PortalImageRull>{
	public List<PortalImageRull> getPortalImageRullListById(int id){
		return this.find("from PortalImageRull where invalid = 0 and id=:p1 order by orderno",new Parameter(id));
	}
	public List<PortalImageRull> getPortalImageRullList(){
		return this.find("from PortalImageRull where invalid = 0  order by orderno");
	}
	public List<PortalImageRull> getPortalImageRullListByType(String type){
		return this.find("from PortalImageRull where  type=:p1 order by orderno",new Parameter(type));
	}
	public List<PortalImageRullModel> getAllType(){
		String sql="select type,typename,invalid,to_char(starttime,'yyyy-MM-dd HH24:mi:ss'),area,typeOrder from sup_portalimagerull t group by type,typename,invalid,typeOrder,to_char(starttime,'yyyy-MM-dd HH24:mi:ss'),area order by typeOrder";
		List<Object[]> list=this.findBySql(sql);
		List<PortalImageRullModel> alist=new ArrayList<PortalImageRullModel>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=0;i<list.size();i++){
			PortalImageRullModel pm=new PortalImageRullModel();
			Object[] obj=list.get(i);
			pm.setType(obj[0].toString());
			pm.setTypeName(obj[1].toString());
			pm.setInvalid(Integer.valueOf(obj[2].toString()));
			if(obj[3]!=null){
				try {
					pm.setStartTime(sdf.format(sdf2.parse(obj[3].toString())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				pm.setStartTime("");
			}
			if(obj[4]!=null){
				pm.setArea(obj[4].toString());
			}else{
				pm.setArea("");
			}
			alist.add(pm);
		}
		return alist;
	}
	public List<Object[]> getProductList(String type){
		String sql="select a.id,b.title,a.orderno,b.datasourse,b.linkurl,b.id as ids,b.IsStatic from sup_portalimagerull a,sup_portalimageproductdef b where a.tid=b.id and  a.type=:p1 order by a.orderno";
		return this.findBySql(sql,new Parameter(type));
	}
	public void updateAll() {
		String sql="update PortalImageRull set invalid=1  ";
		this.update(sql);
	}
	public void updatePortalImageRull(String type) {
		String sql="update PortalImageRull set invalid=0  where type=:p1";
		this.update(sql, new Parameter(type));
	}
}
