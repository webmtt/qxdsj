package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsPushFileRecordExpiredDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushFileRecordExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DdsPushFileRecordExpiredService {

	@Autowired
	private DdsPushFileRecordExpiredDao ddsPushFileRecordExpiredDao;
	
	public Page<DdsPushFileRecordExpired> getDdsPushFileRecordExpiredPage(Page<DdsPushFileRecordExpired> page,
                                                                          DdsPushFileRecordExpired ddsPushFileRecordExpired, String ftpId, String fileName, String pushStatus ) {
				return ddsPushFileRecordExpiredDao.getDdsPushFileRecordExpiredPage(page, ddsPushFileRecordExpired, ftpId, fileName, pushStatus);
		
	}
}
