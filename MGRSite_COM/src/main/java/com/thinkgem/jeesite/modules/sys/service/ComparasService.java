package com.thinkgem.jeesite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.thinkgem.jeesite.modules.index.entity.Emergency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Service
public class ComparasService {

	@Autowired	
	ComparasDao comparasDao;
	public Comparas get(String id) {
		return comparasDao.get(id);
	}
	@Resource(name = "comparasDao")
	public void setComparasDao(ComparasDao comparasDao) {
		this.comparasDao = comparasDao;
	}
	
	@Transactional
	public void save(Comparas comparas){
		comparasDao.save(comparas);
	}
	
	@Transactional
	public List<Comparas> findAll(){
		return comparasDao.findAll();
	}

	public Page<Comparas> findComparas(String description,Page<Comparas> page, Comparas comparas) {

		return comparasDao.findComparasPage(description,page, comparas);
	}
	public Comparas findComparas(String keyid){
		return comparasDao.findComparas(keyid);
	}
	public Comparas getComparas(String keyid) {
		return comparasDao.get(keyid);
	}
	public Object getComparasByKey(String key){
		return comparasDao.getComparasByKey(key);
	}
	@Transactional(readOnly = false)
	public void updateComparasById(String keyid,String stringvalue) {
		comparasDao.updateComparasById(keyid, stringvalue);
	}

	public List<Emergency> getStringValue(String key){
		String str = (String)comparasDao.getComparasByKey(key);
		List<Emergency> list = new ArrayList<Emergency>();
		Emergency  eg= null;
		String[] strs =null;
		if(str!=null&&!"".equals(str)){
			strs=str.split(",");
			for(String provinceStr:strs){
				eg= new Emergency();
				String provinceName=provinceStr.split("##")[0];
				String provinceCode=provinceStr.split("##")[1];
				if("yjProvinceRuler".equals(key)){
					eg.setProvince(provinceName);
					eg.setProvinceCode(provinceCode);
					list.add(eg);
				}
				if("yjTypeRuler".equals(key)){
					eg.setType(provinceName);
					eg.setTypeCode(provinceCode);
					list.add(eg);
				}
				if("yjLevelRuler".equals(key)){
					eg.setLevel(provinceName);
					eg.setLevelCode(provinceCode);
					list.add(eg);
				}
			}
		}
		return list;

	}
}
