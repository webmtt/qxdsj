/*
 * @(#)GroundDataService.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.FriendLinkDao;
import com.thinkgem.jeesite.modules.index.entity.FriendLink;


/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Component
@Transactional(readOnly = false)
public class FriendLinkService extends BaseService{
	@Autowired
	private FriendLinkDao friendlinkdao;
	public FriendLink getFriendLink(Integer id){
		return this.friendlinkdao.get(id);
	}
	public List<FriendLink> getFriendLinkByLinkType(){
		return this.friendlinkdao.getFriendLinkByLinkType();
	}
	public List<FriendLink> getFriendLinkByLinkType(String linktype){
		return friendlinkdao.getFriendLinkByLinkType(linktype);
	}
	public void save(FriendLink friendLink){
		FriendLink friendLink1=friendlinkdao.getByHql("FROM FriendLink where linkid =(select max(linkid) from FriendLink)");
		if(friendLink1==null){
			friendLink.setLinkid(1);
		}else{
			friendLink.setLinkid(friendLink1.getLinkid()+1);
		}
		 friendlinkdao.save(friendLink);
	}
	public void deleteFriendLinkById(String id){
		 this.friendlinkdao.deleteFriendLinkById(id);
	}
}
