/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.subject.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.common.utils.FileUtils;
import com.thinkgem.jeesite.mybatis.modules.report.dao.XuGuSearchDao;
import com.thinkgem.jeesite.mybatis.modules.subject.dao.SupSubjectdefDao;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.ProductImgDef;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.SupSubjectdef;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 专题产品Service
 * @author yangkq
 * @version 2020-02-28
 */
@Service
@Transactional(readOnly = false)
public class SupSubjectdefService extends CrudService<SupSubjectdefDao, SupSubjectdef> {
	@Resource
	private XuGuSearchDao xuguDao;
	@Autowired
	private SupSubjectdefDao dao;
	public SupSubjectdef get(String id) {
		return dao.get(id);
	}

	public List<SupSubjectdef> findList(SupSubjectdef supSubjectdef) {
		return dao.findList(supSubjectdef);
	}

	public void pngImgImport(List<String> list,String pngurl,String pre_url){
		ProductImgDef pi=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHH");
		SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH");
		try{
			//入库处理对象
			List<ProductImgDef> list1=new ArrayList<>();
			File file=null;
			for(int i=0,size=list.size();i<size;i++) {
				String filePath = list.get(i);
				String p=filePath.replace(pngurl, "");
				String[] values=p.split("/");
				file = new File(filePath);
				//yyyyMMddmm
				String fileName = file.getName();
				long fileSize = file.length();
				String extensionName = FileUtils.getExtensionName(fileName);
				pi = new ProductImgDef();
				//要素种类
				pi.setV_ele_kind(values[0]);
				//图片路径
				pi.setD_storage_site(filePath.replace(pre_url,""));
				//图片大小
				pi.setD_file_size(fileSize);
				//图片类型
				pi.setD_file_format(extensionName);
				//图片名称
				pi.setV_file_name(fileName.substring(0, fileName.indexOf(".")));
				String dateName=fileName.substring(0,10);
				Date date = format.parse(dateName);
				//产品时间
				pi.setD_datetime(new Timestamp(date.getTime()));
				String t = format1.format(date);
				String[] ts = t.split(" ");
				//图片日期
				pi.setProdate(ts[0]);
				//发布状态
				pi.setIspublish(1);
				//图片时间-时
				pi.setV04004(Integer.parseInt(ts[1]));
				//文件标识
				pi.setD_file_id(UUID.randomUUID().toString());
				//创建时间
				pi.setD_rymdhm(new Timestamp(file.lastModified()));
				//入库时间
				pi.setD_iymdhm(new Timestamp(System.currentTimeMillis()));
				//更新时间
				pi.setD_update_time(new Timestamp(System.currentTimeMillis()));
				//图片代号
				pi.setV_prod_code(values[1]);
				pi.setD_data_id(Global.getConfig("subject_data_id"));
				list1.add(pi);
			}
			xuguDao.insertIntoProductImg(list1);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public List<TableInfo> findTableInfo(String tablename) {
		List<TableInfo> list=null;
		try{
			String database_id= Global.getConfig("jdbc.url");
			int t=database_id.lastIndexOf("/");
			database_id=database_id.substring(t+1);
			list=dao.tableInfoList(tablename,database_id);
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public boolean checkProcode(String procode,String kind) {
		Integer count=dao.checkProcode(procode,kind);
		if(count==0){
			return true;
		}else {
			return false;
		}
	}

	@Transactional(readOnly = false)
	public void updatepub(SupSubjectdef supSubjectdef) {
		try {
			dao.updatepub(supSubjectdef);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Transactional(readOnly = false)
	public void save(SupSubjectdef supSubjectdef) {
		if (supSubjectdef.getIsNewRecord()){
			supSubjectdef.preInsert();
			dao.insert(supSubjectdef);
		}else{
			supSubjectdef.preUpdate();
			dao.update(supSubjectdef);
		}

		if((!supSubjectdef.getOldProCode().equals(supSubjectdef.getProcode()))||(!supSubjectdef.getOldKind().equals(supSubjectdef.getKind()))){
			if(null!=supSubjectdef.getOldProCode()&&(null!=supSubjectdef.getOldKind())&&(!"".equals(supSubjectdef.getOldProCode()))&&(!"".equals(supSubjectdef.getOldKind()))) {
				//更新产品代号和要素种类
				xuguDao.updateProTable(supSubjectdef.getOldProCode(),supSubjectdef.getOldKind(),supSubjectdef.getProcode(),supSubjectdef.getKind());
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SupSubjectdef supSubjectdef) {
		try {
//			if((!"".equals(supSubjectdef.getTableName()))&&null!=supSubjectdef.getTableName()) {
//				dao.dropPngTable(supSubjectdef.getTableName());
//			}
			dao.delete(supSubjectdef);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<SupSubjectdef> findAllConfig() {
		return dao.findList(new SupSubjectdef());
	}
}