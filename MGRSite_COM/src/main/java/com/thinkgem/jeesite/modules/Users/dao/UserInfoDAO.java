/*
 * @(#)UserInfoDAO.java 2016年3月16日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Repository
public class UserInfoDAO extends BaseDao<UserInfo>{

	public Page<UserInfo> getUserList(String isActive, String userType, int no, int size, String name,String orderBy,String userRankID) {
		Page<UserInfo> plist=new Page<UserInfo>();
		String sql="from UserInfo where delFlag='0'";
		
		if(isActive!=null&&!"".equals(isActive)&&!"5".equals(isActive)){
			sql+=" and isActive="+isActive;
		}
//		if(userType!=null&&!"".equals(userType)){
//			sql+=" and userType="+userType;
//		}
		if (userRankID != null &&!"".equals(userRankID)) {
			sql+=" and userRankID="+userRankID;
		}
		if(name!=null&&!"".equals(name)){
			sql+=" and (userName like '%"+name+"%' or chName like '%"+name+"%' or emailName like '%"+name+"%' or mobile like '%"+name+"%')";
		}
		if (orderBy != null &&!"".equals(orderBy)) {
			//page.setOrderBy("t.CreateTime DESC");
			sql+=" order by "+orderBy;
		}
		//sql+=" order by created DESC";
		plist=this.find(new Page(no,size), sql);
		return plist;
	}

	public UserInfo getUserById(String id) {
		String sql="from UserInfo where iD=:p1";
		List<UserInfo> list=this.find(sql, new Parameter(id));
		if(list!=null&&list.size()>0){
			return list.get(0);			
		}else{
			return null;
		}
	}
	//通过用户名获取
	public UserInfo getUserByUserName(String userName) {
		String sql="from UserInfo where userName=:p1";
		List<UserInfo> list=this.find(sql, new Parameter(userName));
		if(list!=null&&list.size()>0){
			return list.get(0);			
		}else{
			return null;
		}
	}

	public List<UserInfo> getUserListByTime(String stime,String etime) {
		String sql="from UserInfo where created>=to_date(:p1,'yyyymmddHH24miss') and updated<=to_date(:p2,'yyyymmddHH24miss') order by updated";
		return this.find(sql,new Parameter(stime,etime));
			
	}
	public void updateUser(UserInfo userInfo) {
		String sql="update UserInfo set isActive=:p1,updated=:p2,updatedBy=:p3 where iD=:p4";
		this.update(sql, new Parameter(userInfo.getIsActive(),userInfo.getUpdated(),userInfo.getUpdatedBy(),userInfo.getiD()));		
	}

	public void delUserById(String id) {
		String sql="delete from UserInfo where iD=:p1";
		this.update(sql,new Parameter(id));
		
	}
	@Override
	public void save(UserInfo entity) {
		clear();
		super.save(entity);
	}

	public UserInfo getUserInfoByName(String userName) {
		String sql="from UserInfo where upper(userName2)=upper(:p1)";
		List<UserInfo> list=this.find(sql, new Parameter(userName));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public UserInfo getUserInfoByEmail(String userName) {
		String sql="from UserInfo where upper(userName)=upper(:p1)";
		List<UserInfo> list=this.find(sql, new Parameter(userName));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public UserInfo getUserInfoByPhone(String userName) {
		String sql="from UserInfo where upper(userName)=upper(:p1) and delFlag='0'";
		List<UserInfo> list=this.find(sql, new Parameter(userName));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询未绑定角色的用户列表，每次查出10个
	 * @return
	 * @author RainingTime
	 * 2018.7.6
	 * @param userCodeOrName 
	 */
	public List<UserInfo> getUserList(String userCodeOrName) {
		String sql = "from UserInfo where delFlag='0'";
		if(userCodeOrName!=null&&!"".equals(userCodeOrName)){
			sql+=" and (chName like '%"+userCodeOrName+"%' or userName like '%"+userCodeOrName+"%')";
		}
//		sql += " and rownum <= 10";
		return this.find(sql);
	}

	/**
	 * 更改用户角色的绑定状态
	 * @param userid
	 * @param isSpecDatarole
	 * @author RainingTime
	 * 2018.7.6
	 */
	public void updateUserIsSpecDataroleById(String userid, String isSpecDatarole) {
		String sql = "update UserInfo set isSpecDatarole=:p1 where iD=:p2";
		this.update(sql, new Parameter(isSpecDatarole,userid));
	}

	public UserInfo getUserInfoByMobile(String mobile) {
		String sql="from UserInfo where mobile=:p1 and delFlag='0'";
		List<UserInfo> list=this.find(sql, new Parameter(mobile));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

    public List<UserInfo> getUserByDataRoleId(String dataroleid) {
		Session session=this.getSession();
		Query query=null;
		String sql="select * from SUP_USERINFO where delflag='0' and id in (select userid from sup_userdatarole where dataroleid='"+dataroleid+"')";
		query=session.createSQLQuery(sql).addEntity(UserInfo.class);
		return query.list();
    }

	public List<UserInfo> getUsers() {
		Session session=this.getSession();
		Query query=null;
		String sql="select * from SUP_USERINFO where ISACTIVE ='1'";
		query=session.createSQLQuery(sql).addEntity(UserInfo.class);
		return query.list();
	}

	public void updateUrl(String username, String url) {
		String sql="update UserInfo set url=:p2 where userName=:p1";
		this.update(sql, new Parameter(username,url));
	}

	public UserInfo selectUrl(String replace) {
		String sql="from UserInfo where userName=:p1";
		List<UserInfo> list=this.find(sql, new Parameter(replace));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

    public void deleteUserProduct(String id) {
		String sql="update UserInfo set url=:p2 where iD=:p1";
		this.update(sql, new Parameter(id,""));
    }


	/*public void updateIsChecked(String username) {
		String sql="update UserInfo set isChecked=1 where userName=:p1";
		this.update(sql, new Parameter(username));
	}*/

	/*public UserInfo getName(String username) {
		String sql="select * from UserInfo where userName='"+username+"'";
		return this.getByHql(sql);
	}*/
}
