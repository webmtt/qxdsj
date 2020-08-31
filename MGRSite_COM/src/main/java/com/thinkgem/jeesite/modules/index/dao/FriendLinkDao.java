/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.FriendLink;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class FriendLinkDao extends BaseDao<FriendLink>{
	public List<FriendLink> getFriendLinkByLinkType(){
		return this.find("from FriendLink where invalid=0 order by orderno");	
	}
	public List<FriendLink> getFriendLinkByLinkType(String linktype){
		String linktypes[]=linktype.split(",");
		List list=new ArrayList();
		for(int i=0;i<linktypes.length;i++){
			list.add(linktypes[i]);
		}
		return this.find("from FriendLink where invalid=0 and linktype in(:p1) order by orderno",new Parameter(list));	
	}
	public void deleteFriendLinkById(String id){
		String sql="delete from FriendLink where linkid=:p1";
		this.update(sql,new Parameter(Integer.valueOf(id)));
	}
}
