package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.ColumnItemsDefDao;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;

@Service
public class ColumnItemsDefService extends BaseService {
	@Autowired
	private ColumnItemsDefDao columnItemsDefDao;

	public List<ColumnItemsDef> findAll(String url,String imgServerUrl,String ctxRoot, String area) {
		List<ColumnItemsDef> columnItemsDefList = columnItemsDefDao.findAll(area);
		for (ColumnItemsDef columnItemsDef : columnItemsDefList) {
			//imageurl
			if (columnItemsDef.getColumnImageURL() == null) {
				columnItemsDef.setColumnImageURL("");
			}else{
				columnItemsDef.setColumnImageURL(columnItemsDef.getColumnImageURL().trim());
			}
			if((!columnItemsDef.getColumnImageURL().startsWith("http"))&&(!columnItemsDef.getColumnImageURL().equals(""))){
				columnItemsDef.setColumnImageURL(imgServerUrl+columnItemsDef.getColumnImageURL());
			}
			//linkurl
			if (columnItemsDef.getLinkURL() == null) {
				columnItemsDef.setLinkURL("");
			}else{
				columnItemsDef.setLinkURL(columnItemsDef.getLinkURL().trim());
			}
			if(columnItemsDef.getLinkURL().indexOf("<ctx>")!=-1){
				columnItemsDef.setLinkURL(columnItemsDef.getLinkURL().replace("<ctx>", ctxRoot));
			}
		}
		return columnItemsDefList;
	}
	 public List<ColumnItemsDef> find(String area){
		 return columnItemsDefDao.findAll(area);
	 }
	 @Transactional(readOnly = false)
	 public void DelCol(Integer columnItemID,String area){
		 ColumnItemsDef column= columnItemsDefDao.Delcolumn(columnItemID);
		 String areas=column.getAreaItem();
		 String aree=areas.replace(area+",", "");
		 column.setAreaItem(aree);
		 columnItemsDefDao.save(column);
		 
   }
	 public void DelColl(Integer columnItemID){
		 columnItemsDefDao.delById(columnItemID);
		
		 
   }
	 
	 
	 @Transactional(readOnly = false)
	 public void SaveCol(ColumnItemsDef column){
		 columnItemsDefDao.save(column);
	 }
	public  Page<ColumnItemsDef> getByPage(Page<ColumnItemsDef> page,String area){
		return columnItemsDefDao.getByPage(page,area);
	}
	public List<ColumnItemsDef> getTeleList(){
		return columnItemsDefDao.getTeleList();
	}
	 public ColumnItemsDef findByColumn(int id){
		 return columnItemsDefDao.findByColumnItemID(id);
	 }
	public void DelById(int columnItemID){
		columnItemsDefDao.delById(columnItemID);
	}
	public void updataCol(String chnName, String linkURL, Integer showType, int i,String columnType,Integer columnItemID){
		columnItemsDefDao.updateTel(chnName, linkURL, showType, i, columnType, columnItemID);
	}
}
