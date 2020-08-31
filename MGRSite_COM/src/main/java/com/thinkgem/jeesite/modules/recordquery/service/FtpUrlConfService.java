package com.thinkgem.jeesite.modules.recordquery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.recordquery.dao.FtpUrlConfDao;
import com.thinkgem.jeesite.modules.recordquery.entity.FtpUrlConf;


/**
 * ftpUrlCofService
 */
@Component
public class FtpUrlConfService extends BaseService {

	@Autowired
	private FtpUrlConfDao ftpUrlConfDao;
	@Transactional
	public List<FtpUrlConf> findAll() {
		return ftpUrlConfDao.findAll();
	}
	
	
}
