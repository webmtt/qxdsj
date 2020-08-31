package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsPushFileRecordDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushFileRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DdsPushFileRecordService {
	@Autowired
	private DdsPushFileRecordDao ddsPushFileRecordDao;
	public Page<DdsPushFileRecord> getDdsPushFileRecordPage(Page<DdsPushFileRecord> page,
                                                            DdsPushFileRecord dsPushFileRecord, String ftpId, String fileName, String pushStatus ) {
				return ddsPushFileRecordDao.getDdsPushFileRecordPage(page, dsPushFileRecord, ftpId, fileName, pushStatus);
		
	}

}
