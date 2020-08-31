package com.nmpiesat.idata.data.service;

import com.nmpiesat.idata.data.dao.CategoryDataReltDao;
import com.nmpiesat.idata.data.dao.DataCategoryDefDao;
import com.nmpiesat.idata.data.dao.DataDefDao;
import com.nmpiesat.idata.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataCategoryDefService {
    @Autowired
    private DataCategoryDefDao dataCategoryDefDao;
    @Autowired
    private CategoryDataReltDao categoryDataReltDao;
    @Autowired
    private DataDefDao dataDefDao;
    public List<DataCategoryDef> findDataCategoryDefByUid(String userid, String keyword) {

        List<DataCategoryDef> list = dataCategoryDefDao.findByUserId(userid,keyword);
        List<DataCategoryDef> dataCategoryDefList = new ArrayList();
        for (DataCategoryDef dataCategoryDef : list) {
            dataCategoryDef.setLargeiconurl(dataCategoryDef.getLargeiconurl());
            if (dataCategoryDef.getParentid() == 0) {
                // 子级list
                List<DataCategoryDef> dataCategoryDefListSub = new ArrayList();
                for (DataCategoryDef dataCategoryDefSub : list) {
                    if (dataCategoryDefSub.getParentid() == dataCategoryDef.getCategoryid()) {
                        dataCategoryDefListSub.add(dataCategoryDefSub);
                    }
                }
                dataCategoryDef.setList(dataCategoryDefListSub);

                dataCategoryDefList.add(dataCategoryDef);
            }
        }
        return dataCategoryDefList;
    }

    public List<DataCategoryDef> findDataCategoryDef() {
        List<DataCategoryDef> list = dataCategoryDefDao.findAll();
        List<DataCategoryDef> dataCategoryDefList = new ArrayList();
        for (DataCategoryDef dataCategoryDef : list) {
            dataCategoryDef.setLargeiconurl(dataCategoryDef.getLargeiconurl());
            if (dataCategoryDef.getParentid() == 0) {
                // 子级list
                List<DataCategoryDef> dataCategoryDefListSub = new ArrayList();
                for (DataCategoryDef dataCategoryDefSub : list) {
                    if (dataCategoryDefSub.getParentid() == dataCategoryDef.getCategoryid()) {
                        dataCategoryDefListSub.add(dataCategoryDefSub);
                    }
                }
                dataCategoryDef.setList(dataCategoryDefListSub);

                dataCategoryDefList.add(dataCategoryDef);
            }
        }
        return dataCategoryDefList;
    }

    public DataCategoryDef findDataCategoryDefById(int id) {

        List<DataCategoryDef> list=dataCategoryDefDao.findDataCategoryDefById(id);
        DataCategoryDef dataCategoryDef = new DataCategoryDef();
        for (DataCategoryDef categoryDef : list) {
            if (categoryDef.getParentid() == 0) {
                dataCategoryDef = categoryDef;
                List<DataCategoryDef> listSub = new ArrayList();
                for (DataCategoryDef category : list) {
                    if (category.getParentid() == categoryDef.getCategoryid()) {
                        listSub.add(category);
                    }
                }
                dataCategoryDef.setList(listSub);
            }
        }
        return dataCategoryDef;
    }
    public DataCategoryDef findDataCategoryDefByUserId(String userid, int id) {
        List<DataCategoryDef> list = this.dataCategoryDefDao.findDataCategoryDefByUserId(userid, id);
        DataCategoryDef dataCategoryDef = new DataCategoryDef();
        for (DataCategoryDef categoryDef : list) {
            if (categoryDef.getParentid() == 0) {
                dataCategoryDef = categoryDef;
                List<DataCategoryDef> listSub = new ArrayList();
                for (DataCategoryDef category : list) {
                    if (category.getParentid() == categoryDef.getCategoryid()) {
                        listSub.add(category);
                    }
                }
                dataCategoryDef.setList(listSub);
            }
        }
        return dataCategoryDef;
    }
    public DataCategoryDef findDataCategoryDefByIdUnique(int id) {
        DataCategoryDef dataCategoryDef = dataCategoryDefDao.findDataCategoryDefByIdUnique(id);
        return dataCategoryDef;
    }

    public List<DataCategoryDef> findAll() {
        List<DataCategoryDef> list = dataCategoryDefDao.findAll();
        return list;
    }

    public List<DataCateDef> findDataCategory() {
        List<DataCateDef> list=dataCategoryDefDao.findDataCategory();
        return list;
    }

    public List<DataTypeSort> getDataSortCount() {
        //1-基础数据，2-气象均一化，3-网格化，4-元数据
        Map<Integer,DataTypeSort> returnMap=new HashMap<>();
        List<DataCategoryDef> categoryList=dataCategoryDefDao.findDataTypeCount();
       List<String> cacheList=new ArrayList<>();
        //子类
        DataTypeSort dts=null;
        //父类
        DataTypeSort dtp=null;
        for(DataCategoryDef dcd:categoryList){
            String name=dcd.getChnname();
            if(!cacheList.contains(name)){
                cacheList.add(name);
            }
            int index=getCacheListIndex(cacheList,name);
                dts = returnMap.get(index);
                if (dts == null) {
                    dts = new DataTypeSort();
                    dts.setName(name);
                }
                dts.getList().add(dcd.getCategoryid());
                dts=getChildAndParent(dts,dcd);
                returnMap.put(index,dts);

        }
        Collection<DataTypeSort> valueCollection2 = returnMap.values();
        List<DataTypeSort> valueList= new ArrayList<DataTypeSort>(valueCollection2);
        return valueList;
    }
    public List<DataCategoryDef> getNewDataSortCount() {
        List<DataCategoryDef> categoryList=dataCategoryDefDao.findAll();
        for(int i=0,length=categoryList.size();i<length;i++){
            DataCategoryDef dc=categoryList.get(i);
            List<DataDef> dataDefList=dataDefDao.finlistByCateId(dc.getCategoryid());
            dc.setDatacount(dataDefList.size());
        }
        return categoryList;
    }

    private DataTypeSort getChildAndParent(DataTypeSort dts,DataCategoryDef dcd){
        //获取资料列表
        List<CategoryDataRelt> child=categoryDataReltDao.findCategoryDataReltById(dcd.getCategoryid());
        //获取父类
        DataCategoryDef dataCategoryDef = dataCategoryDefDao.findDataCategoryDefByIdUnique(dcd.getParentid());
        DataTypeSort dtp=new DataTypeSort();
        dtp.setName(dataCategoryDef.getChnname());
        dtp.setCount(child.size());
        dtp.setCateGoryId(dataCategoryDef.getCategoryid());
        int count=dts.getCount();
        dts.setCount(count+child.size());
        dts.getParent().add(dtp);
        return dts;
    }
    private int getCacheListIndex(List<String> cacheList,String name){
        int  index = 0;
        for(int i=0,size=cacheList.size();i<size;i++){
            String t=cacheList.get(i);
            if(name.equals(t)){
                index=i;
                break;
            }
        }
        return index;
    }
}
