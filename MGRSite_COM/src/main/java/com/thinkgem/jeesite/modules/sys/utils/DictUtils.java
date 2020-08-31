/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.user.dao.TargetUnitDao;
import com.thinkgem.jeesite.modules.user.entity.TargetUnit;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static TargetUnitDao targetUnitDao = SpringContextHolder.getBean(TargetUnitDao.class);
	public static final String CACHE_DICT_MAP = "dictMap";
	public static final String CACHE_COUNTRYDIC_MAP = "countrydicMap";
	public static final String CACHE_CHINAPROVINCEDIC_MAP = "chinaprovincedicMap";
	public static final String CACHE_FIELDTYPEDIC_MAP = "fieldtypedicMap";
	public static final String CACHE_UNITTYPE_MAP = "unittypeMap";
	public static final String CACHE_SCINCESUBJECTDIC_MAP = "scincesubjectdicMap";
	public static final String CACHE_PROTYPEDIC_MAP = "protypedicMap";
	public static final String CACHE_TRADETYPEDIC_MAP = "tradetypedicMap";
	public static final String CACHE_SEARCHCONDITEMS_MAP = "searchconditemsMap";
	public static final String CACHE_USER_MAP = "userMap";
	public static final String CACHE_PHONECORRES_MAP = "phonecorresMap";
	public static final String CACHE_MDUTYUSER_MAP = "mdutyUserMap";
	public static final String CACHE_LDUTYUSER_MAP = "ldutyUserMap";
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			if(type.equals("user")){
				for (User user : getUserList(type)){
					if (value.equals(user.getLoginName())){
						return user.getName();
					}
				}
			}else if(type.equals("unitId")){
				for (TargetUnit targetUnit : getUnitList(type)){
					if (value.equals(targetUnit.getId())){
						return targetUnit.getName();
					}
				}
			}else{
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		  }
		}
		return defaultValue;
	}
	
	public  static List<User> getUserList(String type) {
		@SuppressWarnings("unchecked")
		Map<String, List<User>> dictMap = (Map<String, List<User>>)CacheUtils.get(CACHE_USER_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (User user : userDao.findAllList()){
				List<User> dictList = dictMap.get(type);
				if (dictList != null){
					dictList.add(user);
				}else{
					dictMap.put(type, Lists.newArrayList(user));
				}
			}
			CacheUtils.put(CACHE_USER_MAP, dictMap);
		}
		List<User> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	public  static List<TargetUnit> getUnitList(String type) {
		@SuppressWarnings("unchecked")
		Map<String, List<TargetUnit>> dictMap = (Map<String, List<TargetUnit>>)CacheUtils.get(CACHE_UNITTYPE_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (TargetUnit targetUnit : targetUnitDao.findAll()){
				List<TargetUnit> dictList = dictMap.get(type);
				if (dictList != null){
					dictList.add(targetUnit);
				}else{
					dictMap.put(type, Lists.newArrayList(targetUnit));
				}
			}
			CacheUtils.put(CACHE_UNITTYPE_MAP, dictMap);
		}
		List<TargetUnit> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList()){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type){
		return JsonMapper.toJsonString(getDictList(type));
	}
}
