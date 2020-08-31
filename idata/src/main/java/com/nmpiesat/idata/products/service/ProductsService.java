package com.nmpiesat.idata.products.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nmpiesat.idata.products.dao.BmpProductinfoDAO;
import com.nmpiesat.idata.products.dao.ProDataindexDAO;
import com.nmpiesat.idata.products.dao.ProductesDao;
import com.nmpiesat.idata.products.entity.*;
import com.nmpiesat.idata.util.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductsService {

    @Autowired
    private ProductesDao productesDao;

    @Autowired
    private ProDataindexDAO proDataindexDAO;

    @Autowired
    private BmpProductinfoDAO bmpProductinfoDAO;

    @Value("${progPath}")
    private String progPath;

    public ProductesConfig selectByPrimaryKey(String id) {
        return productesDao.selectByPrimaryKey(id);
    }

    public void insertProducts(ProductesConfig productes) {
        String uuid = UUID.randomUUID().toString().replace("-","");
        productes.setId(uuid);
        productes.setDelFlag("0");
        productes.setCreate(new Date());
        productes.setUrl(progPath+"#/product/detail/"+uuid);
        productesDao.insert(productes);
    }

    public void updateProducts(ProductesConfig products) {
        productesDao.updateByPrimaryKey(products);
    }

    /**
     * 调用分页插件完成分页
     * @param
     * @return
     */
    public PageInfo<UploadProduct> getProductsFile(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<UploadProduct> list = proDataindexDAO.getProductsFile();
        return new PageInfo<UploadProduct>(list);
    }

    public List<String> getUnit() {
        List<String> list = proDataindexDAO.getUnit();
        return list;
    }

    public List<Map<String,Object>> getUnitSum(String unit,String dataType) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getUnitSum(unit,dataType);
        return list;
    }

    public List<Map<String, Object>> getUnitSumForTimes(String unit,String startTime, String endTime) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getUnitSumForTimes(unit,startTime,endTime);
        return list;
    }

    public List<Map<String, Object>> getUnitCount(String unit, String productname) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getUnitCount(unit,productname);
        return list;
    }

    public int statisticsUnits(String id) {
        int stat = proDataindexDAO.statisticsUnits(id);
        if (stat==1){
            return stat;
        }else{
            return 0;
        }
    }

    public List<Map> getConfig() {
        return productesDao.getConfig();
    }

    public PageInfo<Map<String,Object>> getUnitTypes(int pageNum, int pageSize, String[] alerteunitList, String[] alertetypeList,String[] alerteproductList) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> list =  productesDao.getUnitTypes(alerteunitList,alertetypeList,alerteproductList);
        return new PageInfo<Map<String,Object>>(list);
    }

    public PageInfo<Map<String,Object>> getProduct(int pageNum, int pageSize,String[] forecastunitList,String[] forecasttypeList,String[] forecastproductList) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> list = productesDao.getProduct(forecastunitList,forecasttypeList,forecastproductList);
        return new PageInfo<Map<String,Object>>(list);

    }

    public PageInfo<ProDataindex> getMenuPhotoUT(int pageNum,int pageSize, String[] unitList, String[] typesList,String[] productList,String[] photoTypeList) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProDataindex> list = bmpProductinfoDAO.getMenuPhotoUT(unitList,typesList,productList,photoTypeList);
        return new PageInfo<ProDataindex>(list);
    }

    public List<ProDataindex> getMorePhotoUT(int num, String[] photoTypeList, String[] unitList, String[] typesList,String[] productList) {
        List<ProDataindex> list = bmpProductinfoDAO.getMorePhotoUT(num,photoTypeList,unitList,typesList,productList);
        return list;
    }

    public PageInfo<ProDataindex> getMoreVersionUT(int pageNum,int pageSize,String[] unitList, String[] typesList,String[] productList) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProDataindex> list = proDataindexDAO.getMoreVersionUT(unitList,typesList,productList);
        return new PageInfo<ProDataindex>(list);
    }

    public PageInfo<ProDataindex> getAlltypsFile(int pageNum, int pageSize, String typscode) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProDataindex> list = proDataindexDAO.getAlltypsFile(typscode);
        return new PageInfo<ProDataindex>(list);
    }

    public PageInfo<ProDataindex> getAllProductcodeFile(int pageNum, int pageSize, String productcode) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProDataindex> list = proDataindexDAO.getAllProductcodeFile(productcode);
        return new PageInfo<ProDataindex>(list);
    }

    public ProductesConfig getProductByUrl(String url) {
        return productesDao.getProductByUrl(url);
    }

    public List<Map<String, Object>> getSubType() {
        return proDataindexDAO.getSubType();
    }

    public List<Map<String, Object>> getAllSub(String upType) {
        return proDataindexDAO.getAllSub(upType);
    }

    public Map<String, Object> getProductRepert() {
        return proDataindexDAO.getProductRepert();
    }

    public String getCounts(String unit, String productname) {
        return bmpProductinfoDAO.getCounts(unit,productname);
    }

    public void insertLog(ProductLog productLog) {
        productesDao.insertLog(productLog);
    }

    public void updateOracle(String id) {
        productesDao.updateOracle(id);
    }

    public List<Map<String, Object>> getFactor() {
        return productesDao.getFactor();
    }

    public List<Map<String, Object>> getBmpProductInfo() {
        return productesDao.getBmpProductInfo();
    }

    public List<String> getType(String[] products) {
        return productesDao.getType(products);
    }

    public List<Map<String, Object>> getAllUnitTypes(String type, String[] products) {
        return productesDao.getAllUnitTypes(type,products);
    }

    public Map<String, Object> getTypeName(String typeCode) {
        return productesDao.getTypeName(typeCode);
    }
}
