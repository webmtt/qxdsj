package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchRecord;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchRecordExpired;
import com.thinkgem.jeesite.modules.distribute.service.DdsSearchRecordExpiredService;
import com.thinkgem.jeesite.modules.distribute.service.DdsSearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "dt/ddsSearchRecordController")
public class DdsSearchRecordController extends BaseController {
	@Autowired
	private DdsSearchRecordService ddsSearchRecordService;
	@Autowired
	private DdsSearchRecordExpiredService ddsSearchRecordExpiredService;
	@RequestMapping(value = {"list", ""})
	public String getDdsSearchRecordPage(DdsSearchRecord ddsSearchRecord,String dataId,
			String timeCondBegin,String retriveStatus,Model model,HttpServletResponse response,
			HttpServletRequest request){
			Page<DdsSearchRecord> page =
					ddsSearchRecordService.getDdsSearchRecordPage(new Page<DdsSearchRecord>(request, response),
				ddsSearchRecord, dataId, timeCondBegin, retriveStatus);
			model.addAttribute("page", page);
		    model.addAttribute("dataId", dataId);
		    model.addAttribute("timeCondBegin", timeCondBegin);
		    if(retriveStatus==null || "".equals(retriveStatus)){
				retriveStatus ="3";
			}
		    model.addAttribute("retriveStatus", retriveStatus);
		    return "modules/distribute/DdsSearchRecordList";
		
	}
	@RequestMapping(value = "expiredList")
	public String getDdsSearchRecordExpiredPage(DdsSearchRecordExpired ddsSearchRecordExpired,String dataId,
			String timeCondBegin,String retriveStatus,Model model,HttpServletResponse response,
			HttpServletRequest request){
		Page<DdsSearchRecordExpired> page =
				ddsSearchRecordExpiredService.getDdsSearchRecordExpiredPage(new Page<DdsSearchRecordExpired>(request, response),
						ddsSearchRecordExpired, dataId, timeCondBegin, retriveStatus);
		model.addAttribute("page", page);
	    model.addAttribute("dataId", dataId);
	    model.addAttribute("timeCondBegin", timeCondBegin);
	    if(retriveStatus==null || "".equals(retriveStatus)){
			retriveStatus ="3";
		}
	    model.addAttribute("retriveStatus", retriveStatus);
		return "modules/distribute/DdsSearchRecordExpiredList";
		
	}
}
