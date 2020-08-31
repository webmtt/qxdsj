/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nmpiesat.idata.subject.service;

import com.nmpiesat.idata.subject.dao.SubjectDao;
import com.nmpiesat.idata.subject.entity.ProductImgDef;
import com.nmpiesat.idata.subject.entity.SupSubjectdef;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 专题产品Service
 * @author yangkq
 * @version 2020-02-28
 */
@Service
public class SupSubjectdefService{
	@Autowired
	private SubjectDao dao;
	public Map<String,Object> findAllConfig(String pageNum,String userid) {
		List<SupSubjectdef> listAll=null;
		List<SupSubjectdef> returnData=null;
		if(userid==null) {
			listAll = dao.findMainList(null, null,null);
		}else if(userid!=null){
			listAll = dao.findMainList(null, null,userid);
			if("9999".equals(pageNum)) {
				pageNum = null;
			}

		}
		//总条数
		int total = listAll.size();
		if(pageNum!=null) {
			int currnum=Integer.parseInt(pageNum);
			//每页限制20条
			Integer pageSize = 20;
			//总共的页数
			int beginNum = pageSize * (currnum - 1);
			int endNum = 20;
			int maxPage =
					(int)
							Math.ceil(
									Double.parseDouble(total + "")
											/ Double.parseDouble(pageSize.toString()));
			if (userid == null) {
				returnData = dao.findMainList(beginNum, endNum, null);
			} else {
				returnData = dao.findMainList(beginNum, endNum, userid);
			}
		}else{
			returnData=listAll;
		}
		Map<String,Object> map=new HashedMap();
		map.put("data",returnData);
		map.put("dataCount",total);
        map.put("currentpage",pageNum);
		return map;
	}

    public List<ProductImgDef> findProductImg(String startTime, String endTime,String procode, String kind) {
		List<ProductImgDef> list=dao.findProductImg(kind,procode,startTime,endTime);
		if(list.size()==0){
			list=dao.findProductImg(kind,procode,null,null);
		}
		return list;
    }

	public Map<String,Object> getHomeProduct(String pageNum,String userid) {
		if(userid==null) {
			List<SupSubjectdef> returnData= dao.getHomeProduct();
			Map<String,Object> map=new HashedMap();
			map.put("data",returnData);
			map.put("dataCount",0);
            map.put("currentpage",null);
			return map;
		}else{
			return findAllConfig(pageNum,userid);
		}
	}

	public List<SupSubjectdef> getHotProduct() {
		return dao.getHotProduct();
	}

	public List<SupSubjectdef> getTreeProduct(List<SupSubjectdef> list,String id,String userid,String type) {
		List<SupSubjectdef> list1=null;
		SupSubjectdef suproot=dao.getProductById(id);
		if(userid!=null){
			list1 = dao.getTreeProduct(id, userid);
		}else{
			list1=dao.getTreeProductById(id);
		}
		List<SupSubjectdef> list2 = null;
		if(list1!=null) {
			for (SupSubjectdef s : list1) {
				if(userid!=null){
					list2 = dao.getTreeProduct(s.getId(), userid);
				}else{
					list2=dao.getTreeProductById(s.getId());
				}
				for(SupSubjectdef sup:list2){
					sup.setKind(suproot.getKind());
				}
				s.setChild(list2);
			}
		}
		return list1;
	}
	public List<SupSubjectdef> getTreeProductFree(List<SupSubjectdef> list,String ids,String userid,String type) {
		List<SupSubjectdef> list1=null;
		String[] idss=ids.split(",");
		List<String> listids=new ArrayList<>(idss.length);
		Collections.addAll(listids,idss);
		//获取一级目录结构
		list1=dao.getTreeProductFree(listids);
		if(list1!=null) {
			List<SupSubjectdef> list2=new ArrayList<>();
			List<SupSubjectdef> list3=null;
			for (SupSubjectdef s : list1) {
				//获取子集目录
				list3=getTreeProduct( list2,s.getId(),userid,type);
				s.setChild(list3);
			}
		}
		return list1;
	}

	public List<SupSubjectdef> getFeaturesProduct() {
		return dao.getFeaturesProduct();
	}

	public boolean ispermissions(String id, String userid) {
		List<String> list=dao.ispermissions(userid);
		if(list.contains(id)){
			return true;
		}else {
			return false;
		}
	}
}