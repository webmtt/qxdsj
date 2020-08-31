package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.access.dao.AccessFuncDateInfoDao;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncDateInfo;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessFuncDateInfoService extends BaseService {
	@Autowired
	private AccessFuncDateInfoDao accessfuncdateinfodao;

	public Map<String,Integer> findByDatePVNum(String sdate,String edate,String showType) {
		Map<String,Integer> dataMap = new HashMap();
		if(edate==null||"".equals(edate)){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDatePVNum(sdate,showType);
			for (AccessFuncInfo afdi : list) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getPvnumber());
				}else {
					dataMap.put(itemid, afdi.getPvnumber());
				}
			}
		}else {
			List<AccessFuncDateInfo> list = this.accessfuncdateinfodao.findByDatePVNum(sdate,edate,showType);
			for (AccessFuncDateInfo afdi : list) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getPvnumber());
				}else {
					dataMap.put(itemid, afdi.getPvnumber());
				}
			}			
		}
		return dataMap;
	}
	
	public Map<String,Integer> findByDateIPNum(String date,String...edate) {
		Map<String,Integer> dataMap = new HashMap();
		if(edate.length==0){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDateIPNum(date);
			for (AccessFuncInfo afdi : list) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getIpnum());
				}else {
					dataMap.put(itemid, afdi.getIpnum());
				}
			}
		}else {
			List<AccessFuncDateInfo> listInfo = this.accessfuncdateinfodao.findByDateIPNum(date,edate[0]);
			for (AccessFuncDateInfo afdi : listInfo) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getIpnum());
				}else {
					dataMap.put(itemid, afdi.getIpnum());
				}
			}
		}
		return dataMap;
	}
	
	public Map<String,Integer> findByDateStayTime(String date,String edate,String showType) {
		Map<String,Integer> relMap = new HashMap();
		Map<String, AccessFuncInfo> dataMap = new HashMap();
		if(edate==null||"".equals(edate)){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDateStayTime(date,showType);
			for (AccessFuncInfo afdi : list) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				
				if(dataMap.containsKey(itemid)){
					AccessFuncInfo ac = dataMap.get(itemid);
					ac.setStaytime(ac.getStaytime()+afdi.getStaytime()*afdi.getPvnumber());
					ac.setPvnumber(ac.getPvnumber()+afdi.getPvnumber());
				}else {
					AccessFuncInfo ac = new AccessFuncInfo();
					ac.setPvnumber(afdi.getPvnumber());
					ac.setStaytime(afdi.getStaytime()*afdi.getPvnumber());
					dataMap.put(itemid,ac);
				}
			}
		}else {
			List<AccessFuncDateInfo> list = this.accessfuncdateinfodao.findByDateStayTime(date,edate,showType);
			for (AccessFuncDateInfo afdi : list) {
				String itemid = afdi.getItemid();
				if (!itemid.equals("999999")) {
					itemid = afdi.getItemid().substring(0, 1);
				}
				
				if(dataMap.containsKey(itemid)){
					AccessFuncInfo ac = dataMap.get(itemid);
					ac.setStaytime(ac.getStaytime()+afdi.getStaytime()*afdi.getPvnumber());
					ac.setPvnumber(ac.getPvnumber()+afdi.getPvnumber());
				}else {
					AccessFuncInfo ac = new AccessFuncInfo();
					ac.setPvnumber(afdi.getPvnumber());
					ac.setStaytime(afdi.getStaytime()*afdi.getPvnumber());
					dataMap.put(itemid,ac);
				}
			}
		}
		
		for(String itemid:dataMap.keySet()){
			AccessFuncInfo ac  = (AccessFuncInfo)dataMap.get(itemid);
			if(ac.getPvnumber()!=null&&ac.getPvnumber()!=0){
				relMap.put(itemid, ac.getStaytime()/ac.getPvnumber());
			}
		}
		return relMap;
	}

	public Map<String, Integer> findByDatePVNumSub(String sdate,String edate,String showType,String menuId) {
		Map<String,Integer> dataMap = new HashMap();
		if(edate==null||"".equals(edate)){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDatePVNum(sdate,showType);
			for (AccessFuncInfo afdi : list) {
				String itemid;
				if(afdi.getItemid().length()>menuId.length()+2){
					itemid = afdi.getItemid().substring(0, menuId.length()+2);
				}else{
					itemid = afdi.getItemid();
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getPvnumber());
				}else {
					dataMap.put(itemid, afdi.getPvnumber());
				}
			}
		}else {
			List<AccessFuncDateInfo> list = this.accessfuncdateinfodao.findByDatePVNum(sdate,edate,showType);
			/*for(int i = 0; i < list.size(); i++){
				if(list.get(i).getItemid().equals("201"))
					System.out.print(list.get(i).getItemid() + " ");
			}*/
			for (AccessFuncDateInfo afdi : list) {
				//String itemid = afdi.getItemid().substring(0, menuId.length()+2);
				String itemid;
				if(afdi.getItemid().length()>menuId.length()+2){
					itemid = afdi.getItemid().substring(0, menuId.length()+2);
				}else{
					itemid = afdi.getItemid();
				}
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getPvnumber());
				}else {
					dataMap.put(itemid, afdi.getPvnumber());
				}
			}			
		}
		return dataMap;
	}

	public Map<String,Integer> findByDateIPNumSub(String date,String...edate) {
		Map<String,Integer> dataMap = new HashMap();
		if(edate.length==0){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDateIPNum(date);
			for (AccessFuncInfo afdi : list) {
				String itemid = afdi.getItemid();
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getIpnum());
				}else {
					dataMap.put(itemid, afdi.getIpnum());
				}
			}
		}else {
			List<AccessFuncDateInfo> listInfo = this.accessfuncdateinfodao.findByDateIPNum(date,edate[0]);
			for (AccessFuncDateInfo afdi : listInfo) {
				String itemid = afdi.getItemid();
				if(dataMap.containsKey(itemid)){
					dataMap.put(itemid,dataMap.get(itemid)+afdi.getIpnum());
				}else {
					dataMap.put(itemid, afdi.getIpnum());
				}
			}
		}
		return dataMap;
	}
	
	public Map<String,Integer> findByDateStayTimeSub(String date,String edate,String showType,String menuId) {
		Map<String,Integer> relMap = new HashMap();
		Map<String, AccessFuncInfo> dataMap = new HashMap();
		if(edate==null||"".equals(edate)){
			List<AccessFuncInfo> list = this.accessfuncdateinfodao.findByDateStayTime(date,showType);
			for (AccessFuncInfo afdi : list) {
				String itemid;
				if(afdi.getItemid().length()>menuId.length()+2){
					itemid = afdi.getItemid().substring(0, menuId.length()+2);
				}else{
					itemid = afdi.getItemid();
				}
				if(dataMap.containsKey(itemid)){
					AccessFuncInfo ac = dataMap.get(itemid);
					ac.setStaytime(ac.getStaytime()+afdi.getStaytime()*afdi.getPvnumber());
					ac.setPvnumber(ac.getPvnumber()+afdi.getPvnumber());
				}else {
					AccessFuncInfo ac = new AccessFuncInfo();
					ac.setPvnumber(afdi.getPvnumber());
					ac.setStaytime(afdi.getStaytime()*afdi.getPvnumber());
					dataMap.put(itemid,ac);
				}
			}
		}else {
			List<AccessFuncDateInfo> list = this.accessfuncdateinfodao.findByDateStayTime(date,edate,showType);
			for (AccessFuncDateInfo afdi : list) {
				String itemid;
				if(afdi.getItemid().length()>menuId.length()+2){
					itemid = afdi.getItemid().substring(0, menuId.length()+2);
				}else{
					itemid = afdi.getItemid();
				}
				if(dataMap.containsKey(itemid)){
					AccessFuncInfo ac = dataMap.get(itemid);
					ac.setStaytime(ac.getStaytime()+afdi.getStaytime()*afdi.getPvnumber());
					ac.setPvnumber(ac.getPvnumber()+afdi.getPvnumber());
				}else {
					AccessFuncInfo ac = new AccessFuncInfo();
					ac.setPvnumber(afdi.getPvnumber());
					ac.setStaytime(afdi.getStaytime()*afdi.getPvnumber());
					dataMap.put(itemid,ac);
				}
			}
		}
		
		for(String itemid:dataMap.keySet()){
			AccessFuncInfo ac  = (AccessFuncInfo)dataMap.get(itemid);
			if(ac.getPvnumber()!=null&&ac.getPvnumber()!=0){
				relMap.put(itemid, ac.getStaytime()/ac.getPvnumber());
			}
		}
		return relMap;
	}
	/*public Map<String, int[]> findByDateStayTime(String date1, String date2) {
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		//int[] ipnum = new int[10];
		int[] pvnum = new int[10];
		int[] staytime = new int[10];
		List<AccessFuncDateInfo> listInfo = this.accessfuncdateinfodao.findByDateStayTime(date1, date2);
		for (AccessFuncDateInfo afd : listInfo) {
			if (afd.getItemid().equals("999999")) {
				//ipnum[0] += afd.getIpnum();
				pvnum[0] += afd.getPvnumber();
				staytime[0] += afd.getStaytime() * afd.getPvnumber();
			} else {
				String temp = afd.getItemid().substring(0, 1);
				//ipnum[Integer.parseInt(temp)] += afd.getIpnum();
				pvnum[Integer.parseInt(temp)] += afd.getPvnumber();
				staytime[Integer.parseInt(temp)] += afd.getStaytime() * afd.getPvnumber();
			}
		}
		for(int i = 0; i < 10; i++){
			if(pvnum[i] != 0){
				staytime[i] = staytime[i] / pvnum[i];
			}
		}
		Map<String, int[]> map = new HashMap<String, int[]>();
		//map.put("ipnum", ipnum);
		//map.put("pvnum", pvnum);
		map.put("staytime", staytime);
		return map;
	}*/
}