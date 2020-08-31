/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.utils.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService  {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;


	//-- User Service --//
	
	public User getUser(String id) {
		return userDao.get(id);
	}

	public Page<User> findUser(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()){
			dc.add(Restrictions.ne("id", "1")); 
		}
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%"+user.getLoginName()+"%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%"+user.getName()+"%"));
		}
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		page.setOrderBy("update_date desc");
		return userDao.find(page, dc);
	}

	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}
	public User getUserByNo(String no) {
		return userDao.getUserByNo(no);
	}


	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.clear();
		user.setUpdate_date(DateUtils.formatDateTime(new Date()));
		userDao.save(user);
		CacheUtils.remove(DictUtils.CACHE_USER_MAP);
		systemRealm.clearAllCachedAuthorizationInfo();
		// 同步到Activiti
	}

	@Transactional(readOnly = false)
	public void deleteUser(String id) {
		userDao.deleteById(id);
		// 同步到Activiti
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(String id) {
		userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
	}

	@Transactional(readOnly = false)
	public void deleteRole(String id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);		
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		if("".equals(menu.getTarget())) {
			menu.setTarget(null);
		}
		//清理缓存数据
		menuDao.clear();
		//权限清空
//		menu.setTarget(null);
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
	}
	public List<User> findAllList() {
		return userDao.findAllList();
	}
	public  List<User> getUserByRoleId(String id){
		List<User> list=userDao.getUserByRoleId(id);
		return list;
	}
	
}