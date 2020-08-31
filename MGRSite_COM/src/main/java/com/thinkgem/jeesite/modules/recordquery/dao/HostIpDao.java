package com.thinkgem.jeesite.modules.recordquery.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.recordquery.entity.HostIp;

@Repository
public class HostIpDao extends BaseDao<HostIp> {
	@Override
	public List<HostIp> findAll() {
		return this.find("from HostIp order by name");
	}
}
