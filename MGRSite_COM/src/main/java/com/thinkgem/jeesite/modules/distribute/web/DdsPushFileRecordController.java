package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushConfInfo;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushFileRecord;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushFileRecordExpired;
import com.thinkgem.jeesite.modules.distribute.service.DdsPushConfInfoService;
import com.thinkgem.jeesite.modules.distribute.service.DdsPushFileRecordExpiredService;
import com.thinkgem.jeesite.modules.distribute.service.DdsPushFileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value="dt/DdsPushFileRecordController")
public class DdsPushFileRecordController extends BaseController {
	
	@Autowired
	private DdsPushFileRecordService ddsPushFileRecordService;
	@Autowired
	private DdsPushFileRecordExpiredService ddsPushFileRecordExpiredService;
	@Autowired
	private DdsPushConfInfoService ddsPushConfInfoService;
	@RequestMapping(value = {"list", ""})
	public String getDdsPushFileRecordPage(DdsPushFileRecord ddsPushFileRecord,String ftpId,
			String fileName,String pushStatus,String dataId,Model model,HttpServletResponse response,
			HttpServletRequest request){
		if(dataId!=null&!"".equals(dataId)){
			List<DdsPushConfInfo> list= ddsPushConfInfoService.findDdsPushConfInfoListByDataId(dataId);
			if(list.size()==0){
				ftpId ="";//查不到DdsPushConfInfo数据，防止页面报500错误，处理一下，使查询结果为空
			}else{
				ftpId = list.get(0).getFtpId();	
				fileName = fileName.split("&")[0];
				
			}
		}
		Page<DdsPushFileRecord> page =
				ddsPushFileRecordService.getDdsPushFileRecordPage(new Page<DdsPushFileRecord>(request, response),
						ddsPushFileRecord, ftpId, fileName, pushStatus);
		model.addAttribute("page", page);
	    model.addAttribute("ftpId", ftpId);
	    model.addAttribute("fileName", fileName);
	    if(pushStatus==null || "".equals(pushStatus)){
			pushStatus ="3";
		}
	    model.addAttribute("pushStatus", pushStatus);
	    return "modules/distribute/DdsPushFileRecordList";
	}
	
	@RequestMapping(value = "expiredList")
	public String getDdsPushFileRecordExpiredPage(DdsPushFileRecordExpired ddsPushFileRecordExpired, String ftpId,
                                                  String fileName, String pushStatus, Model model, HttpServletResponse response,
                                                  HttpServletRequest request){
		
		Page<DdsPushFileRecordExpired> page =
				ddsPushFileRecordExpiredService.getDdsPushFileRecordExpiredPage(new Page<DdsPushFileRecordExpired>(request, response),
						ddsPushFileRecordExpired, ftpId, fileName, pushStatus);
		model.addAttribute("page", page);
	    model.addAttribute("ftpId", ftpId);
	    model.addAttribute("fileName", fileName);
	    if(pushStatus==null || "".equals(pushStatus)){
			pushStatus ="3";
		}
	    model.addAttribute("pushStatus", pushStatus);
	    return "modules/distribute/DdsPushFileRecordExpiredList";
	}
	@RequestMapping(value="findFtpId")
	public void findFtpId(String dataId,String fileName,DdsPushFileRecord ddsPushFileRecord){
		List<DdsPushConfInfo> list= ddsPushConfInfoService.findDdsPushConfInfoListByDataId(dataId);
		DdsPushConfInfo ddsPushConfInfo = list.get(0);
	
	}
	
	

}
