package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsSearchRecordDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DdsSearchRecordService {
	@Autowired
	private DdsSearchRecordDao ddsSearchRecordDao;
	public Page<DdsSearchRecord> getDdsSearchRecordPage(Page<DdsSearchRecord> page, DdsSearchRecord ddsSearchRecord,
                                                        String dataId, String timeCondBegin, String retriveStatus) {
		return ddsSearchRecordDao.getDdsSearchRecordPage(page, ddsSearchRecord, dataId, timeCondBegin, retriveStatus);
	}
}
