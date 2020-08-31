package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsSearchRecordExpiredDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchRecordExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DdsSearchRecordExpiredService {

	@Autowired
	private DdsSearchRecordExpiredDao ddsSearchRecordExpiredDao ;
	public Page<DdsSearchRecordExpired> getDdsSearchRecordExpiredPage(Page<DdsSearchRecordExpired> page, DdsSearchRecordExpired ddsSearchRecordExpired,
                                                                      String dataId, String timeCondBegin, String retriveStatus) {
		return ddsSearchRecordExpiredDao.getDdsSearchRecordExpiredPage(page, ddsSearchRecordExpired, dataId, timeCondBegin, retriveStatus);
	}
}
