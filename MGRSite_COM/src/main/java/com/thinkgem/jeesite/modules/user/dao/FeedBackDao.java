/*
 * @(#)FeedBackDAO.java 2016年5月30日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.user.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.user.entity.UserFeedBack;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年5月30日
 */
@Repository
public class FeedBackDao extends BaseDao<UserFeedBack>{
	@Override
	public void save(UserFeedBack entity) {
		super.save(entity);
	}
	public Page<UserFeedBack> findUserFeedBackPage(Page<UserFeedBack> page, UserFeedBack userFeedBack, String orderBy,String fDStatus) {
		if (orderBy == null || "".equals(orderBy)) {
			page.setOrderBy("created DESC");
		}
		StringBuffer sb = new StringBuffer("from UserFeedBack where 1=1 and invalid='0'");
		
		return this.find(page, sb.toString(), new Parameter());
	}
	public List<UserFeedBack> getFeedBackListById(String userId) {
		return this.find("from UserFeedBack where userID=:p1 order by fDTime desc", new Parameter(userId));
	}

	public void delFeedBackById(String id) {
		this.update("update UserFeedBack set invalid='1' where id=:p1 ",new Parameter(id));
	}

	public UserFeedBack getFeedBackByIDS(String id, String userId) {
		String sql="from UserFeedBack where id=:p1 and userID=:p2";
		List<UserFeedBack> list=this.find(sql, new Parameter(id,userId));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public void updateFeed(String title, String context, String id, String userId, Timestamp update) {
		String sql="update UserFeedBack set fDTitle=:p1,fDContext=:p2,updated=:p3 where id=:p4  and userID=:p5";
		this.update(sql, new Parameter(title,context,update,id,userId));		
	}
	
}
