/*
 * @(#)DataCategoryDAO.java 2016年9月26日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年9月26日
 */
@Repository
public class DataCategoryDAO extends BaseDao<DataCategory>{
	@Override
	public void save(DataCategory entity) {
		super.save(entity);
	}

	public DataCategory getDataCategoryById(Integer categoryid) {
		String sql="from DataCategory where categoryid=:p1 ";
		List<DataCategory> list=this.find(sql, new Parameter(categoryid));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public List<DataCategory> getDataCategoryList(Integer parentid) {
		String sql="from DataCategory where parentid=:p1  order by orderno";
		List<DataCategory> list=this.find(sql, new Parameter(parentid));
		return list;
	}
	public List<DataCategory> getDataCategoryList2(Integer parentid) {
		String sql="from DataCategory where parentid=:p1 and categoryid!=17 order by orderno";
		List<DataCategory> list=this.find(sql, new Parameter(parentid));
		return list;
	}

	public void upload(List<List<Object>> list){
	    String sql = null;
	    for(int i = 0; i< list.size(); i++){
	        List<Object> ob = list.get(i);
            sql =
                    "insert into BMD_DATACATEGORYDEF(categoryid,chnname,chndescription,imageurl,categorylayer,parentid,orderno,invalid,showtype,SHOWUSERRANKID,iconurl,largeiconurl,middleiconurl)values( '"
                            +ob.get(0)
                            + "','"
                            +ob.get(1)
                            +"','"
                            +ob.get(2)
                            +"','"
                            +ob.get(3)
                            +"','"
                            +ob.get(4)
                            +"','"
                            +ob.get(5)
                            +"','"
                            +ob.get(6)
                            +"','"
                            +ob.get(7)
                            +"','"
                            +ob.get(8)
                            +"','"
                            +0
                            +"','"
                            +ob.get(9)
                            +"','"
                            +ob.get(10)
                            +"','"
                            + ob.get(11)
                            +"')";
            this.saveTest(sql);
        }
    }

    private void saveTest(String sql) {
        this.getSession().createSQLQuery(sql).executeUpdate();
        this.getSession().flush(); // 清理缓存，执行批量插入
        this.getSession().clear(); // 清空缓存中的 对象
    }
}
