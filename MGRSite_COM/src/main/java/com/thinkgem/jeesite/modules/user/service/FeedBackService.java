/*
 * @(#)FeedBackService.java 2016年5月30日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.user.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.user.dao.FeedBackDao;
import com.thinkgem.jeesite.modules.user.entity.UserFeedBack;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年5月30日
 */
@Service
public class FeedBackService {
	@Autowired
	private FeedBackDao feedBackDao;
	public UserFeedBack getUserFeedBack(String id) {
		return feedBackDao.get(id);
	}
	@Transactional
	public List<UserFeedBack> getFeedBackListById(String userId) {
		return feedBackDao.getFeedBackListById(userId);
	}
	@Transactional
	public void save(UserFeedBack userFeedBack) {
		 feedBackDao.save(userFeedBack);
	}
	public Page<UserFeedBack> findUserFeedBackPage(Page<UserFeedBack> page, UserFeedBack userFeedBack,String orderBy,String fDStatus) {
		DetachedCriteria dc = feedBackDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(userFeedBack.getfDTitle())){
			dc.add(Restrictions.like("fDTitle", "%"+userFeedBack.getfDTitle()+"%"));
		}
		if (userFeedBack.getUnitId()!=0){
			dc.add(Restrictions.like("unitId", userFeedBack.getUnitId()));
		}
		if (fDStatus!=null&&!"".equals(fDStatus)){
			dc.add(Restrictions.like("fDStatus", Integer.valueOf(fDStatus)));
		}
		dc.add(Restrictions.like("invalid", 0));
		//查询有效的
		return feedBackDao.find(page,dc);
		//return feedBackDao.findUserFeedBackPage(page, userFeedBack, orderBy,fDStatus);
	}
	@Transactional
	public void delFeedBackById(String id) {
		feedBackDao.delFeedBackById(id);
	}
	public UserFeedBack getFeedBackByIDS(String id, String userId) {
		return feedBackDao.getFeedBackByIDS(id,userId);
	}
	@Transactional
	public void updateFeedBackInfo(String title, String context, String id, String userId) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime=new Date(System.currentTimeMillis());
		feedBackDao.updateFeed(title,context,id,userId,Timestamp.valueOf(sdf.format(dateTime)));
	}

}
