/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@Repository
public class UserDao extends BaseDao<User> {
	
	public List<User> findAllList() {
		return find("from User  order by id", new Parameter());
	}
	public List<User> findList() {
		return find("from User  order by id", new Parameter());
	}
	public User findByLoginName(String loginName){
		return getByHql("from User where loginName = :p1 and delFlag = :p2", new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}

	public int updatePasswordById(String newPassword, String id){
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}
	
	public int updateLoginInfo(String loginIp, Date loginDate, String id){
		return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3", new Parameter(loginIp, loginDate, id));
	}

	public User getUserByNo(String no) {
		return getByHql("from User where no=:p1 and delFlag=:p2",new Parameter(no,User.DEL_FLAG_NORMAL));
	}

    public List<User> getUserByRoleId(String id) {
		Query query = null;
		String sql = "select * from SUP_AdminUser where DEL_FLAG='0' AND id in (select user_id from sup_adminuser_role where role_id='"+id+"')"; //设置自己的sql语句，条件语句可以更复杂
		Session session=this.getSession();
		query = session.createSQLQuery(sql).addEntity(User.class); //返回实体类的方法

		return query.list();
    }
}
