package com.thinkgem.jeesite.modules.recordquery.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlConf;
/**
 * ftpUrlCof DAO接口
 */
@Repository
public class FtpUrlConfDao extends BaseDao<FtpUrlConf> {
	public List<FtpUrlConf> findAll(){
		return this.find("from FtpUrlConf");
	}
	
}
