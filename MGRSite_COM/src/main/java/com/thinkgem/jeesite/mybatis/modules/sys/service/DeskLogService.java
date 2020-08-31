/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.modules.sys.dao.DeskLogDao;
import com.thinkgem.jeesite.mybatis.modules.sys.entity.DeskLog;
import com.thinkgem.jeesite.mybatis.modules.sys.entity.Log;

/**
 * 前台日志Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DeskLogService extends CrudService<DeskLogDao, DeskLog> {

	public Page<DeskLog> findPage(Page<DeskLog> page, DeskLog deskLog) {
		
		// 设置默认时间范围，默认当前月
		if (deskLog.getBeginDate() == null){
			deskLog.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (deskLog.getEndDate() == null){
			deskLog.setEndDate(DateUtils.addMonths(deskLog.getBeginDate(), 1));
		}
		
		return super.findPage(page, deskLog);
		
	}
	
}
